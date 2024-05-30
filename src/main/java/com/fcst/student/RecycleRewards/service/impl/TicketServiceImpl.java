package com.fcst.student.RecycleRewards.service.impl;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.TicketService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private static final int TICKET_EXPIRATION_HOURS = 72;
    private static final int TICKET_NUMBER_LENGTH = 8;
    private static final String TICKET_NUMBER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final TicketRepository ticketRepository;
    private final UserService userService;

    public TicketServiceImpl(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    private void setAttributesAndModel(HttpSession session, Model model, Ticket ticket, Integer bottlesCount, Boolean isVoucher) {
        String issuedOnFormatted = formatDate(ticket.getIssued());
        String expirationFormatted = formatDate(ticket.getActiveUntil());

        setSessionAttributes(session, ticket.getNumber(), issuedOnFormatted, expirationFormatted, bottlesCount, isVoucher);
        setModelAttributes(model, ticket.getNumber(), issuedOnFormatted, expirationFormatted, bottlesCount, isVoucher);
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
    }

    private void setSessionAttributes(HttpSession session, String ticketNumber, String issuedOn, String expiration, Integer bottlesCount, Boolean isVoucher) {
        session.setAttribute("ticketNumber", ticketNumber);
        session.setAttribute("issuedOnFormatted", issuedOn);
        session.setAttribute("expirationFormatted", expiration);
        session.setAttribute("bottlesCount", bottlesCount);
        session.setAttribute("isVoucher", isVoucher);
    }

    private void setModelAttributes(Model model, String ticketNumber, String issuedOn, String expiration, Integer bottlesCount, Boolean isVoucher) {
        model.addAttribute("ticketNumber", ticketNumber);
        model.addAttribute("issuedOnFormatted", issuedOn);
        model.addAttribute("expirationFormatted", expiration);
        model.addAttribute("bottlesCount", bottlesCount);
        model.addAttribute("isVoucher", isVoucher);
    }

    @Override
    public Integer getDefaultBottlesCount(Integer bottlesCount) {
        return Optional.ofNullable(bottlesCount).orElse(0);
    }

    @Override
    public String openStartPage(HttpSession session, Model model) {
        Integer bottlesCount = getDefaultBottlesCount((Integer) session.getAttribute("bottlesCount"));
        Boolean isVoucher = (Boolean) session.getAttribute("isVoucher");
        model.addAttribute("bottlesCount", bottlesCount);
        model.addAttribute("isVoucher", isVoucher);
        return "start";
    }

    @Override
    public String openPrintPage(HttpSession session, Model model) {
        Integer bottlesCount = getDefaultBottlesCount((Integer) session.getAttribute("bottlesCount"));
        String ticketNumber = (String) session.getAttribute("ticketNumber");

        if (ticketNumber == null) {
            ticketNumber = generateUniqueTicketNumber();
            session.setAttribute("ticketNumber", ticketNumber);
        }

        Boolean isVoucher = (Boolean) session.getAttribute("isVoucher");
        Ticket ticket = createTicket(bottlesCount, ticketNumber, isVoucher);

        setAttributesAndModel(session, model, ticket, bottlesCount, isVoucher);

        return "print";
    }

    @Override
    public String openRegisterTicketPage(HttpSession session, Model model) {
        Boolean loggedIn = Optional.ofNullable((Boolean) session.getAttribute("loggedIn")).orElse(false);
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            userService.getUserById(userId).ifPresent(user -> {
                model.addAttribute("loggedUser", user);
                session.setAttribute("loggedUser", user);
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
                session.setAttribute("totalPoints", totalPoints);
            });
        }
        return "registerTicket";
    }

    @Override
    public ResponseEntity<Map<String, Object>> setVoucherAttribute(Map<String, Boolean> requestBody, HttpSession session) {
        Boolean isVoucher = requestBody.get("isVoucher");
        session.setAttribute("isVoucher", isVoucher);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @Override
    public void addBottle(HttpSession session) {
        Integer bottlesCount = getDefaultBottlesCount((Integer) session.getAttribute("bottlesCount"));
        bottlesCount++;
        session.setAttribute("bottlesCount", bottlesCount);
    }

    @Override
    public String exitSession(HttpSession session, Model model) {
        Integer bottlesCount = getDefaultBottlesCount((Integer) session.getAttribute("bottlesCount"));
        String ticketNumber = (String) session.getAttribute("ticketNumber");

        Boolean isVoucher = (Boolean) session.getAttribute("isVoucher");
        Ticket ticket = createTicket(bottlesCount, ticketNumber, isVoucher);

        setAttributesAndModel(session, model, ticket, bottlesCount, isVoucher);

        saveTicket(ticket);
        session.setAttribute("bottlesCount", 0);
        session.invalidate();

        model.addAttribute("alertMessage", "Printing...");

        return "print_with_alert";
    }

    @Override
    public ResponseEntity<String> registerTicket(String ticketNumber, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<User> currentUserOpt = userService.getUserById(userId);
            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                Optional<Ticket> ticketOpt = ticketRepository.findByNumber(ticketNumber);
                if (ticketOpt.isPresent() && !ticketOpt.get().getVoucher()) {
                    Ticket ticket = ticketOpt.get();
                    if (ticket.getRegisteredOn() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Кодът е вече регистриран. Моля опитайте с друг код.");
                    }

                    if (isTicketExpired(ticket)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Времето за регистрация на този билет е изтекло. Моля, опитайте с друг билет.");
                    }

                    registerTicketToUser(ticket, currentUser, session, model);

                    return ResponseEntity.ok(ticket.getPoints() + " точки са добавени успешно към Вашия профил");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Билетът не беше намерен. Моля, въведете валиден номер.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не сте оторизиран за тази операция. Моля, влезте в профила си и опитайте отново.");
    }

    private boolean isTicketExpired(Ticket ticket) {
        LocalDateTime currentTime = LocalDateTime.now();
        long hoursDifference = ChronoUnit.HOURS.between(ticket.getIssued(), currentTime);
        return hoursDifference > TICKET_EXPIRATION_HOURS;
    }

    private void registerTicketToUser(Ticket ticket, User user, HttpSession session, Model model) {
        ticket.setRegisteredOn(LocalDateTime.now());
        ticket.setRegisteredBy(user);

        int currentBottlesCount = ticket.getPoints() / 5;
        user.setTotalBottles(user.getTotalBottles() + currentBottlesCount);
        int userTotalBottles = user.getTotalBottles();

        ticketRepository.save(ticket);

        int pointsToAdd = ticket.getPoints();
        int totalPoints = user.getTotalPoints() + pointsToAdd;
        user.setTotalPoints(totalPoints);
        userService.saveUser(user);

        model.addAttribute("userTotalBottles", userTotalBottles);
        session.setAttribute("totalBottles", userTotalBottles);
        session.setAttribute("totalPoints", totalPoints);
    }

    @Override
    public Ticket createTicket(Integer bottlesCount, String ticketNumber, Boolean isVoucher) {
        if (ticketNumber == null || ticketNumber.isEmpty()) {
            ticketNumber = generateUniqueTicketNumber();
        }
        LocalDateTime issuedOn = LocalDateTime.now();
        LocalDateTime expirationDateTime = issuedOn.plusHours(TICKET_EXPIRATION_HOURS);
        int points = isVoucher ? 0 : bottlesCount * 5;

        return new Ticket(ticketNumber, issuedOn, expirationDateTime, bottlesCount, points, isVoucher);
    }

    @Override
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public String generateUniqueTicketNumber() {
        Random random = new Random();

        while (true) {
            String ticketNumber = random.ints(TICKET_NUMBER_LENGTH, 0, TICKET_NUMBER_ALPHABET.length())
                    .mapToObj(TICKET_NUMBER_ALPHABET::charAt)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();

            if (!ticketNumberExistsInDatabase(ticketNumber)) {
                return ticketNumber;
            }
        }
    }

    private boolean ticketNumberExistsInDatabase(String ticketNumber) {
        return ticketRepository.existsByNumber(ticketNumber);
    }
}
