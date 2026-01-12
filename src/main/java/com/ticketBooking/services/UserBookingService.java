package com.ticketBooking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ticketBooking.entities.Ticket;
import com.ticketBooking.entities.Train;
import com.ticketBooking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketBooking.util.UserServiceUtil;


public class UserBookingService {

    private User user;
    private List<User> userList;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE)
    .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String USER_PATH = "D:\\Ankit Coding Stuff\\Java\\localDb\\users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        loadUser();
    }
    public UserBookingService() throws IOException {
        loadUser();
    }

    private void loadUser() throws IOException {
        File users = new File(USER_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});

    }

    public boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 ->{
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

//    public boolean signUp(User user1){
//        try{
//            userList.add(user1);
//            saveUserListToFile();
//            return Boolean.TRUE;
//        }catch (IOException ex){
//            ex.printStackTrace();
//            return Boolean.FALSE;
//        }
//
//
//    }

    public synchronized boolean signUp(User user1) {
        if (user1 == null) {
            System.err.println("signUp called with null user");
            return false;
        }
        System.out.println("Signing up (instance=" + this + "): " + user1.getName());
        try {
            userList.add(user1);
            System.out.println("In-memory userList size now = " + userList.size() +
                    ", last username = " + userList.get(userList.size()-1).getName());

            saveUserListToFile();
            return true;
        } catch (Exception ex) {
            System.err.println("Error during signUp/save: " + ex.getClass().getName() + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }


//    private void saveUserListtoFile() throws IOException {
//        File userFile = new File(USER_PATH);
//        objectMapper.writeValue(userFile, userList);
//    }

    // Call this instead of your old saveUserListtoFile()
    private void saveUserListToFile() throws IOException {
        Path p = Paths.get(USER_PATH); // keep your current USER_PATH
        Path parent = p.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
            System.out.println("Created parent dir: " + parent.toAbsolutePath());
        }
        // 1) Show JSON that will be written
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList);
        System.out.println("=== JSON BEING WRITTEN (first 4000 chars) ===");
        System.out.println(json.length() > 4000 ? json.substring(0,4000) + "\n...(truncated)" : json);
        System.out.println("=== END JSON ===");

        // 2) Write debug backup (so you can inspect old/new)
        Path bak = p.resolveSibling(p.getFileName().toString() + ".bak");
        Files.write(bak, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Wrote debug backup to: " + bak.toAbsolutePath() + " (size=" + Files.size(bak) + ")");

        // 3) Atomic write to target
        Path temp = Files.createTempFile(parent, "users", ".tmp");
        Files.write(temp, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        Files.move(temp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        System.out.println("Atomic move complete to: " + p.toAbsolutePath() + " (size=" + Files.size(p) + ")");

        // 4) Immediate read-back verification
        try {
            List<User> verify = objectMapper.readValue(p.toFile(), new TypeReference<List<User>>() {});
            System.out.println("Verify read-back: size=" + verify.size() +
                    ", last username=" + (verify.isEmpty() ? "none" : verify.get(verify.size()-1).getName()));
        } catch (Exception ex) {
            System.err.println("Failed to read-back file after write: " + ex.getClass().getName() + " - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void fetchBooking(){
        user.printTicket();
    }

    public Boolean cancelBooking(String ticketId) throws IOException {
        return null;
    }

    public List<Train> getTrains(String source, String destination){
        TrainService trainService = null;
        try {
            trainService = new TrainService();
            return trainService.searchTrain(source, destination);
        } catch (IOException e) {
            return new ArrayList<>();
        }

    }


    public List<List<Integer>> fetchSeat(Train trainSelectedForBooking) {
        return trainSelectedForBooking.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int col) {
        TrainService trainService = null;
        try {
            trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if(row>0 && row<seats.size() && col>0 && col< seats.size()){
                if(seats.get(row).get(col) == 0){
                    seats.get(row).set(col, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                }
            }
            return false;
        } catch (IOException e) {
            return Boolean.FALSE;
        }

    }
}
