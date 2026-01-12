package com.ticketBooking;

import com.ticketBooking.entities.Train;
import com.ticketBooking.entities.User;
import com.ticketBooking.services.UserBookingService;
import com.ticketBooking.util.UserServiceUtil;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Running Trian Booking System");
        Scanner scanner = new Scanner(System.in);
        int option =0;

        UserBookingService userBookingService;

        try{
            userBookingService = new UserBookingService();

        } catch (IOException e) {
            System.out.println("There is something worng");
            e.printStackTrace();
            return;
        }

        while(option != 7){
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();
            Train trainSelectedForBooking = new Train();
            switch (option){
                case 1:
                    System.out.println("Enter the username to Signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignup = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                    System.out.println(userBookingService.signUp(userToSignup));
                    break;

                case 2:
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());
                    try{
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (IOException e) {
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Type your source station");
                    String source = scanner.next();
                    System.out.println("Type your destination station");
                    String dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source, dest);
                    int index =1;
                    for(Train t: trains){
                        System.out.println(index +" Train ID : "+ t.getTrainId());
                        for(Map.Entry<String, String> entry: t.getStationTimes().entrySet()){
                            System.out.println("Station "+entry.getKey()+" time: "+entry.getValue());
                        }
                    }
                    System.out.println("Select a train by typing 1,2,3...");
                    trainSelectedForBooking = trains.get(scanner.nextInt()-1);
                    break;
                case 5:
                    System.out.println("Select a seat out of these seats");
                    List<List<Integer>> seats = userBookingService.fetchSeat(trainSelectedForBooking);
                    for (List<Integer> row : seats){
                        for(Integer val : row){
                            System.out.println(val+ " ");
                        }
                        System.out.println();
                    }
                    System.out.println("select the seat by select row and column");
                    System.out.println("Enter the row");
                    int row = scanner.nextInt();
                    System.out.println("Enter the col");
                    int col = scanner.nextInt();
                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                    if (booked.equals(Boolean.TRUE)) {
                        System.out.println("Booked! Enjoy your Journey");
                    }else{
                        System.out.println("Can't book this seat");
                    }
                    break;


            }


        }
    }

}