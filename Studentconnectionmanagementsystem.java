package com.studentconnection.interfaces;

public interface Trackable {
    void updateStatus(String status);
    String getCurrentStatus();
}
package com.studentconnection.model;

import com.studentconnection.interfaces.Trackable;

public class Connection implements Trackable {
    private String connectionId;
    private Insider insider;
    private Outsider outsider;
    private Requirement requirement;
    private String status;
    private double connectionFee;
    private boolean isRated;
    
    public Connection(String connectionId, Insider insider, Outsider outsider, Requirement requirement) {
        this.connectionId = connectionId;
        this.insider = insider;
        this.outsider = outsider;
        this.requirement = requirement;
        this.status = "Pending";
        this.connectionFee = calculateFee();
        this.isRated = false;
    }
    
    private double calculateFee() {
        return requirement.getDeliveryOption().equalsIgnoreCase("Express") ? 15.0 : 10.0;
    }
    
    @Override
    public void updateStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String getCurrentStatus() {
        return status;
    }
    
    // Getters and Setters
    public String getConnectionId() { return connectionId; }
    public Insider getInsider() { return insider; }
    public Outsider getOutsider() { return outsider; }
    public Requirement getRequirement() { return requirement; }
    public double getConnectionFee() { return connectionFee; }
    public boolean isRated() { return isRated; }
    public void setRated(boolean rated) { isRated = rated; }
}
package com.studentconnection.model;

public abstract class Student {
    private String name;
    private String phoneNumber;
    private String department;
    private String rollNumber;
    private double rating = 5.0;
    private int rewardPoints = 0;
    
    public Student(String name, String phoneNumber, String department, String rollNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.rollNumber = rollNumber;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDepartment() { return department; }
    public String getRollNumber() { return rollNumber; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getRewardPoints() { return rewardPoints; }
    public void addRewardpoints(int points) { this.rewardPoints += points; }
    
    public abstract String getStudentType();
}
package com.studentconnection.model;

public class Insider extends Student {
    public Insider(String name, String phoneNumber, String department, String rollNumber) {
        super(name, phoneNumber, department, rollNumber);
    }
    
    @Override
    public String getStudentType() {
        return "Insider";
    }
}
package com.studentconnection.model;

public class Outsider extends Student {
    public Outsider(String name, String phoneNumber, String department, String rollNumber) {
        super(name, phoneNumber, department, rollNumber);
    }
    
    @Override
    public String getStudentType() {
        return "Outsider";
    }
}
package com.studentconnection.model;

public class Requirement {
    private final String itemName;
    private final String category;
    private final String description;
    private final String deliveryOption;
    private final double estimatedCost;
    
    public Requirement(String itemName, String category, String description, 
                      String deliveryOption, double estimatedCost) {
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.deliveryOption = deliveryOption;
        this.estimatedCost = estimatedCost;
    }
    
    // Getters
    public String getItemName() { return itemName; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDeliveryOption() { return deliveryOption; }
    public double getEstimatedCost() { return estimatedCost; }
}
package com.studentconnection.service;

import com.studentconnection.model.*;
import java.util.*;

public class ConnectionService {
    private final List<Outsider> outsiders = new ArrayList<>();
    private final List<Insider> insiders = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private double companyIncome = 0.0;
    private static int connectionCounter = 1000;
    private final Random random = new Random();
    
    public void registerOutsider(Outsider outsider) {
        outsiders.add(outsider);
    }
    
    public void registerInsider(Insider insider) {
        insiders.add(insider);
    }
    
    public Connection createConnection(Insider insider, Requirement requirement) {
        if (outsiders.isEmpty()) return null;
        
        Outsider outsider = outsiders.get(random.nextInt(outsiders.size()));
        Connection connection = new Connection(
            "CONN-" + (connectionCounter++), 
            insider, 
            outsider, 
            requirement
        );
        
        connections.add(connection);
        companyIncome += connection.getConnectionFee();
        insider.addRewardpoints(10);
        outsider.addRewardpoints(10);
        
        return connection;
    }
    
    // Getters
    public List<Connection> getConnections() { return Collections.unmodifiableList(connections); }
    public double getCompanyIncome() { return companyIncome; }
    public List<Outsider> getOutsiders() { return Collections.unmodifiableList(outsiders); }
    public List<Insider> getInsiders() { return Collections.unmodifiableList(insiders); }
}
package com.studentconnection.util;

public class Validator {
    public static boolean isValidRollNumber(String rollNumber) {
        return rollNumber != null && rollNumber.matches("CB\\.SC\\.U4CSE\\d{5}");
    }
}