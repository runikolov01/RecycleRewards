package com.fcst.student.RecycleRewards.model;

import com.fcst.student.RecycleRewards.model.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "user_code")
    private Long userCode;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    @Column(name = "is_activated")
    private boolean isActivated = false;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "total_points", nullable = false)
    private int totalPoints;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(unique = true)
    private Integer phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "prize_winners",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "prize_id"))
    private List<Prize> prizes;

    @Column(name = "total_bottles")
    private int totalBottles;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public User() {
    }

    public User(Long id, Long userCode, String firstName, String lastName, Integer totalPoints, Address address, String email, String password, Integer phone, Role role, List<Prize> prizes) {
        this.id = id;
        this.userCode = userCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalPoints = totalPoints;
        this.address = address;
        this.email = email;
        this.password = password;
        this.registrationDate = LocalDateTime.now();
        this.phone = phone;
        this.role = role;
        this.prizes = prizes;
    }

    public User(Long id, Long userCode, String activationToken, LocalDateTime tokenExpiry, boolean isActivated, String firstName, String lastName, int totalPoints, Address address, String email, String password, LocalDateTime registrationDate, Integer phone, Role role, List<Prize> prizes, int totalBottles) {
        this.id = id;
        this.userCode = userCode;
        this.activationToken = activationToken;
        this.tokenExpiry = tokenExpiry;
        this.isActivated = isActivated;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalPoints = totalPoints;
        this.address = address;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.phone = phone;
        this.role = role;
        this.prizes = prizes;
        this.totalBottles = totalBottles;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

//    public Long getAddressId() {
//        return addressId;
//    }
//
//    public void setAddressId(Long addressId) {
//        this.addressId = addressId;
//    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void addPrize(Prize prize) {
        prizes.add(prize);
    }

    public int getTotalBottles() {
        return totalBottles;
    }

    public void setTotalBottles(int totalBottles) {
        this.totalBottles = totalBottles;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }
}