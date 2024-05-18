package com.fcst.student.RecycleRewards.web;

import com.fcst.student.RecycleRewards.model.Ticket;
import com.fcst.student.RecycleRewards.model.User;
import com.fcst.student.RecycleRewards.repository.TicketRepository;
import com.fcst.student.RecycleRewards.service.TicketService;
import com.fcst.student.RecycleRewards.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Controller
public class TicketController {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    @Autowired
    private TicketService ticketService;

    public TicketController(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    private void setAttributesAndModel(HttpSession session, Model model, Ticket ticket, Integer bottlesCount) {
        String issuedOnFormatted = ticket.getIssued().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        String expirationFormatted = ticket.getActiveUntil().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        session.setAttribute("ticketNumber", ticket.getNumber());
        session.setAttribute("issuedOnFormatted", issuedOnFormatted);
        session.setAttribute("expirationFormatted", expirationFormatted);

        model.addAttribute("bottlesCount", bottlesCount);
        model.addAttribute("ticketNumber", ticket.getNumber());
        model.addAttribute("issuedOnFormatted", issuedOnFormatted);
        model.addAttribute("expirationFormatted", expirationFormatted);
    }

    @GetMapping("/index")
    public String openHomePage() {
        return "index";
    }

    @GetMapping("/start")
    public String openStartPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);

        model.addAttribute("bottlesCount", bottlesCount);
        return "start";
    }

    @GetMapping("/print")
    public String openPrintPage(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        String ticketNumber = (String) session.getAttribute("ticketNumber");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);

        if (ticketNumber == null) {
            ticketNumber = ticketService.generateUniqueTicketNumber(); // Generate a new ticket number if not already generated
            session.setAttribute("ticketNumber", ticketNumber); // Store it in the session
        }

        Ticket ticket = ticketService.createTicket(bottlesCount, ticketNumber);
        setAttributesAndModel(session, model, ticket, bottlesCount);

        return "print";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // Check if the user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = authentication != null && authentication.isAuthenticated();

        // Add the loggedIn attribute to the model
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);

                // Fetch totalPoints for the logged-in user and add it to the model
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
            System.out.println(user.getRole());
        }
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Fetch user details from the database using the user ID
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);
                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
            }
        }
        return "about";
    }


    @GetMapping("/registerTicket")
    public String registerTicket(Model model, HttpSession session) {
        // Check if the user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = authentication != null && authentication.isAuthenticated();

        // Add the loggedIn attribute to the model
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                model.addAttribute("loggedUser", user);

                Integer totalPoints = user.getTotalPoints();
                model.addAttribute("totalPoints", totalPoints);
                session.setAttribute("totalPoints", totalPoints);
            }
        }
        return "registerTicket";
    }

    @PostMapping("/start")
    public String addBottle(HttpSession session) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);
        bottlesCount++;

        session.setAttribute("bottlesCount", bottlesCount);

        return "redirect:/start";
    }

    @PostMapping("/print")
    public String exitSession(HttpSession session, Model model) {
        Integer bottlesCount = (Integer) session.getAttribute("bottlesCount");
        bottlesCount = ticketService.getDefaultBottlesCount(bottlesCount);
        String ticketNumber = (String) session.getAttribute("ticketNumber");

        Ticket ticket = ticketService.createTicket(bottlesCount, ticketNumber);
        setAttributesAndModel(session, model, ticket, bottlesCount);

        ticketService.saveTicket(ticket);
        session.setAttribute("bottlesCount", 0);
        session.invalidate();

        // Passing the alert message as a parameter
        model.addAttribute("alertMessage", "Printing...");

        return "print_with_alert";
    }

    @PostMapping("/registerTicket")
    public ResponseEntity<String> registerTicket(@RequestParam("ticketNumber") String ticketNumber,
                                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Fetch user details from the database using the user ID
            User currentUser = userService.getUserById(userId);
            if (currentUser != null) {
                Ticket ticket = ticketRepository.findByNumber(ticketNumber);
                if (ticket != null) {
                    // Check if the ticket is already registered
                    if (ticket.getRegisteredOn() != null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Кодът е вече регистриран. Моля опитайте с друг код.");
                    }

                    // Calculate the time difference
                    LocalDateTime currentTime = LocalDateTime.now();
                    LocalDateTime ticketIssuedTime = ticket.getIssued();
                    long hoursDifference = ChronoUnit.HOURS.between(ticketIssuedTime, currentTime);

                    if (hoursDifference > 72) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Времето за регистрация на този билет е изтекло. Моля, опитайте с друг билет.");
                    }

                    ticket.setRegisteredOn(LocalDateTime.now());
                    ticket.setRegisteredBy(currentUser);
                    ticketRepository.save(ticket);

                    int pointsToAdd = ticket.getPoints();
                    int currentTotalPoints = currentUser.getTotalPoints();
                    int totalPoints = currentTotalPoints + pointsToAdd;
                    currentUser.setTotalPoints(totalPoints);
                    userService.saveUser(currentUser);

                    session.setAttribute("totalPoints", totalPoints);


                    return ResponseEntity.ok(ticket.getPoints() + " точки са добавени успешно към Вашия профил");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Билетът не беше намерен. Моля, въведете валиден номер.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Не сте оторизиран за тази операция. Моля, влезте в профила си и опитайте отново.");
    }
}