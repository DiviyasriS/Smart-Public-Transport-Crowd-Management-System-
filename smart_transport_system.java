// pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.smarttransport</groupId>
    <artifactId>crowd-management-system</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.0</version>
        <relativePath/>
    </parent>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>nz.ac.waikato.cms.weka</groupId>
            <artifactId>weka-stable</artifactId>
            <version>3.8.6</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

// ===== APPLICATION MAIN CLASS =====

package com.smarttransport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class SmartTransportApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartTransportApplication.class, args);
    }
}

// ===== ENTITY CLASSES =====

package com.smarttransport.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String vehicleNumber;
    
    @Column(nullable = false)
    private String routeId;
    
    @Column(nullable = false)
    private String vehicleType; // BUS, TRAIN, METRO
    
    private int capacity;
    private int currentOccupancy;
    private double latitude;
    private double longitude;
    private LocalDateTime lastUpdated;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(String vehicleNumber, String routeId, String vehicleType, int capacity) {
        this.vehicleNumber = vehicleNumber;
        this.routeId = routeId;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.currentOccupancy = 0;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public int getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(int currentOccupancy) { this.currentOccupancy = currentOccupancy; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public double getOccupancyRate() {
        return capacity > 0 ? (double) currentOccupancy / capacity : 0.0;
    }
}

package com.smarttransport.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "travel_patterns")
public class TravelPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String routeId;
    private String stopId;
    private int hourOfDay;
    private int dayOfWeek;
    private int averageOccupancy;
    private LocalDateTime recordedAt;
    
    // Constructors
    public TravelPattern() {}
    
    public TravelPattern(String routeId, String stopId, int hourOfDay, 
                        int dayOfWeek, int averageOccupancy) {
        this.routeId = routeId;
        this.stopId = stopId;
        this.hourOfDay = hourOfDay;
        this.dayOfWeek = dayOfWeek;
        this.averageOccupancy = averageOccupancy;
        this.recordedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getStopId() { return stopId; }
    public void setStopId(String stopId) { this.stopId = stopId; }
    
    public int getHourOfDay() { return hourOfDay; }
    public void setHourOfDay(int hourOfDay) { this.hourOfDay = hourOfDay; }
    
    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public int getAverageOccupancy() { return averageOccupancy; }
    public void setAverageOccupancy(int averageOccupancy) { this.averageOccupancy = averageOccupancy; }
    
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}

// ===== DTOs =====

package com.smarttransport.dto;

public class LocationUpdateDto {
    private String vehicleNumber;
    private double latitude;
    private double longitude;
    private int currentOccupancy;
    
    // Constructors
    public LocationUpdateDto() {}
    
    public LocationUpdateDto(String vehicleNumber, double latitude, 
                            double longitude, int currentOccupancy) {
        this.vehicleNumber = vehicleNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentOccupancy = currentOccupancy;
    }
    
    // Getters and Setters
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public int getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(int currentOccupancy) { this.currentOccupancy = currentOccupancy; }
}

package com.smarttransport.dto;

import java.util.List;

public class RouteRecommendationDto {
    private String routeId;
    private String routeName;
    private int estimatedTime;
    private double crowdLevel; // 0.0 to 1.0
    private List<String> stops;
    private String reason;
    
    // Constructors
    public RouteRecommendationDto() {}
    
    public RouteRecommendationDto(String routeId, String routeName, 
                                 int estimatedTime, double crowdLevel, 
                                 List<String> stops, String reason) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.estimatedTime = estimatedTime;
        this.crowdLevel = crowdLevel;
        this.stops = stops;
        this.reason = reason;
    }
    
    // Getters and Setters
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    
    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }
    
    public double getCrowdLevel() { return crowdLevel; }
    public void setCrowdLevel(double crowdLevel) { this.crowdLevel = crowdLevel; }
    
    public List<String> getStops() { return stops; }
    public void setStops(List<String> stops) { this.stops = stops; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

// ===== REPOSITORIES =====

package com.smarttransport.repository;

import com.smarttransport.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);
    List<Vehicle> findByRouteId(String routeId);
    
    @Query("SELECT v FROM Vehicle v WHERE v.routeId = ?1 ORDER BY v.currentOccupancy ASC")
    List<Vehicle> findByRouteIdOrderByOccupancy(String routeId);
    
    @Query("SELECT AVG(v.currentOccupancy) FROM Vehicle v WHERE v.routeId = ?1")
    Double getAverageOccupancyByRoute(String routeId);
}

package com.smarttransport.repository;

import com.smarttransport.entity.TravelPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TravelPatternRepository extends JpaRepository<TravelPattern, Long> {
    List<TravelPattern> findByRouteIdAndHourOfDayAndDayOfWeek(
        String routeId, int hourOfDay, int dayOfWeek);
    
    @Query("SELECT AVG(t.averageOccupancy) FROM TravelPattern t " +
           "WHERE t.routeId = ?1 AND t.hourOfDay = ?2 AND t.dayOfWeek = ?3")
    Double getPredictedOccupancy(String routeId, int hourOfDay, int dayOfWeek);
}

// ===== SERVICES =====

package com.smarttransport.service;

import com.smarttransport.entity.Vehicle;
import com.smarttransport.dto.LocationUpdateDto;
import com.smarttransport.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleTrackingService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public void updateVehicleLocation(LocationUpdateDto locationUpdate) {
        try {
            // Send to Kafka for real-time processing
            String message = objectMapper.writeValueAsString(locationUpdate);
            kafkaTemplate.send("vehicle-locations", locationUpdate.getVehicleNumber(), message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @KafkaListener(topics = "vehicle-locations")
    public void processLocationUpdate(String message) {
        try {
            LocationUpdateDto locationUpdate = objectMapper.readValue(message, LocationUpdateDto.class);
            
            Optional<Vehicle> vehicleOpt = vehicleRepository.findByVehicleNumber(
                locationUpdate.getVehicleNumber());
            
            if (vehicleOpt.isPresent()) {
                Vehicle vehicle = vehicleOpt.get();
                vehicle.setLatitude(locationUpdate.getLatitude());
                vehicle.setLongitude(locationUpdate.getLongitude());
                vehicle.setCurrentOccupancy(locationUpdate.getCurrentOccupancy());
                vehicle.setLastUpdated(LocalDateTime.now());
                
                vehicleRepository.save(vehicle);
                
                // Send real-time update to WebSocket clients
                messagingTemplate.convertAndSend("/topic/vehicles/" + vehicle.getRouteId(), vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Vehicle> getVehiclesByRoute(String routeId) {
        return vehicleRepository.findByRouteId(routeId);
    }
    
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
    
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}

package com.smarttransport.service;

import com.smarttransport.entity.TravelPattern;
import com.smarttransport.entity.Vehicle;
import com.smarttransport.repository.TravelPatternRepository;
import com.smarttransport.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrowdPredictionService {
    
    @Autowired
    private TravelPatternRepository travelPatternRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    private LinearRegression model;
    private Instances dataset;
    
    public void initializeMLModel() {
        try {
            // Create attributes for ML model
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("hourOfDay"));
            attributes.add(new Attribute("dayOfWeek"));
            attributes.add(new Attribute("routeId"));
            attributes.add(new Attribute("occupancy")); // target variable
            
            dataset = new Instances("TravelPatterns", attributes, 0);
            dataset.setClassIndex(dataset.numAttributes() - 1);
            
            // Load historical data
            List<TravelPattern> patterns = travelPatternRepository.findAll();
            for (TravelPattern pattern : patterns) {
                Instance instance = new DenseInstance(4);
                instance.setValue(0, pattern.getHourOfDay());
                instance.setValue(1, pattern.getDayOfWeek());
                instance.setValue(2, Double.parseDouble(pattern.getRouteId().replaceAll("[^0-9]", "1")));
                instance.setValue(3, pattern.getAverageOccupancy());
                dataset.add(instance);
            }
            
            // Train model
            model = new LinearRegression();
            if (dataset.numInstances() > 0) {
                model.buildClassifier(dataset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public double predictCrowdDensity(String routeId, int hourOfDay, int dayOfWeek) {
        try {
            if (model == null) {
                initializeMLModel();
            }
            
            // First try historical average
            Double historicalAvg = travelPatternRepository.getPredictedOccupancy(
                routeId, hourOfDay, dayOfWeek);
            
            if (historicalAvg != null) {
                return Math.min(historicalAvg / 100.0, 1.0); // Convert to 0-1 scale
            }
            
            // Use ML model if available
            if (model != null && dataset.numInstances() > 0) {
                Instance instance = new DenseInstance(4);
                instance.setValue(0, hourOfDay);
                instance.setValue(1, dayOfWeek);
                instance.setValue(2, Double.parseDouble(routeId.replaceAll("[^0-9]", "1")));
                instance.setDataset(dataset);
                
                double prediction = model.classifyInstance(instance);
                return Math.min(Math.max(prediction / 100.0, 0.0), 1.0);
            }
            
            // Fallback: current average occupancy
            Double currentAvg = vehicleRepository.getAverageOccupancyByRoute(routeId);
            return currentAvg != null ? Math.min(currentAvg / 100.0, 1.0) : 0.5;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0.5; // Default moderate crowd level
        }
    }
    
    public void recordTravelPattern(String routeId, String stopId, int occupancy) {
        LocalDateTime now = LocalDateTime.now();
        TravelPattern pattern = new TravelPattern(
            routeId, stopId, now.getHour(), now.getDayOfWeek().getValue(), occupancy);
        travelPatternRepository.save(pattern);
    }
    
    public double getCurrentRouteCrowdLevel(String routeId) {
        List<Vehicle> vehicles = vehicleRepository.findByRouteId(routeId);
        if (vehicles.isEmpty()) return 0.0;
        
        double totalOccupancyRate = vehicles.stream()
            .mapToDouble(Vehicle::getOccupancyRate)
            .average()
            .orElse(0.0);
            
        return Math.min(totalOccupancyRate, 1.0);
    }
}

package com.smarttransport.service;

import com.smarttransport.dto.RouteRecommendationDto;
import com.smarttransport.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RouteOptimizationService {
    
    @Autowired
    private CrowdPredictionService crowdPredictionService;
    
    @Autowired
    private VehicleTrackingService vehicleTrackingService;
    
    // Mock route data - in real implementation, this would come from a database
    private final Map<String, List<String>> routeStops = new HashMap<>();
    private final Map<String, String> routeNames = new HashMap<>();
    private final Map<String, Integer> routeTimes = new HashMap<>();
    
    public RouteOptimizationService() {
        initializeMockRoutes();
    }
    
    private void initializeMockRoutes() {
        // Route 1: Main Line
        routeStops.put("R001", Arrays.asList("Central Station", "Business District", 
                                            "University", "Shopping Mall", "Airport"));
        routeNames.put("R001", "Main Line Express");
        routeTimes.put("R001", 45);
        
        // Route 2: Circle Line
        routeStops.put("R002", Arrays.asList("Downtown", "Hospital", "Park View", 
                                            "Stadium", "Tech Park", "Downtown"));
        routeNames.put("R002", "Circle Line");
        routeTimes.put("R002", 60);
        
        // Route 3: Suburban
        routeStops.put("R003", Arrays.asList("Central Station", "Residential Area", 
                                            "School Zone", "Community Center", "Suburbs"));
        routeNames.put("R003", "Suburban Express");
        routeTimes.put("R003", 35);
    }
    
    public List<RouteRecommendationDto> getRouteRecommendations(
            String fromStop, String toStop) {
        
        List<RouteRecommendationDto> recommendations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (String routeId : routeStops.keySet()) {
            List<String> stops = routeStops.get(routeId);
            
            // Check if route serves both stops
            if (stops.contains(fromStop) && stops.contains(toStop)) {
                double crowdLevel = crowdPredictionService.predictCrowdDensity(
                    routeId, now.getHour(), now.getDayOfWeek().getValue());
                
                // Get current crowd level for better accuracy
                double currentCrowdLevel = crowdPredictionService.getCurrentRouteCrowdLevel(routeId);
                double avgCrowdLevel = (crowdLevel + currentCrowdLevel) / 2.0;
                
                String reason = generateRecommendationReason(avgCrowdLevel);
                
                RouteRecommendationDto recommendation = new RouteRecommendationDto(
                    routeId,
                    routeNames.get(routeId),
                    routeTimes.get(routeId),
                    avgCrowdLevel,
                    stops,
                    reason
                );
                
                recommendations.add(recommendation);
            }
        }
        
        // Sort by crowd level (less crowded first) and then by time
        recommendations.sort((r1, r2) -> {
            int crowdComparison = Double.compare(r1.getCrowdLevel(), r2.getCrowdLevel());
            return crowdComparison != 0 ? crowdComparison : 
                   Integer.compare(r1.getEstimatedTime(), r2.getEstimatedTime());
        });
        
        return recommendations;
    }
    
    private String generateRecommendationReason(double crowdLevel) {
        if (crowdLevel < 0.3) {
            return "Low crowd expected - comfortable journey";
        } else if (crowdLevel < 0.6) {
            return "Moderate crowd - decent travel experience";
        } else if (crowdLevel < 0.8) {
            return "High crowd expected - consider alternative time";
        } else {
            return "Very crowded - strongly recommend alternative route/time";
        }
    }
    
    public Map<String, Object> getRouteStatistics(String routeId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Vehicle> vehicles = vehicleTrackingService.getVehiclesByRoute(routeId);
        
        if (!vehicles.isEmpty()) {
            double avgOccupancy = vehicles.stream()
                .mapToDouble(Vehicle::getOccupancyRate)
                .average()
                .orElse(0.0);
            
            stats.put("averageOccupancy", avgOccupancy);
            stats.put("totalVehicles", vehicles.size());
            stats.put("availableVehicles", 
                vehicles.stream().mapToLong(v -> v.getOccupancyRate() < 0.8 ? 1 : 0).sum());
        } else {
            stats.put("averageOccupancy", 0.0);
            stats.put("totalVehicles", 0);
            stats.put("availableVehicles", 0);
        }
        
        return stats;
    }
}

// ===== CONTROLLERS =====

package com.smarttransport.controller;

import com.smarttransport.dto.LocationUpdateDto;
import com.smarttransport.entity.Vehicle;
import com.smarttransport.service.VehicleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
    
    @Autowired
    private VehicleTrackingService vehicleTrackingService;
    
    @PostMapping("/location-update")
    public ResponseEntity<String> updateLocation(@RequestBody LocationUpdateDto locationUpdate) {
        vehicleTrackingService.updateVehicleLocation(locationUpdate);
        return ResponseEntity.ok("Location updated successfully");
    }
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByRoute(@PathVariable String routeId) {
        List<Vehicle> vehicles = vehicleTrackingService.getVehiclesByRoute(routeId);
        return ResponseEntity.ok(vehicles);
    }
    
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleTrackingService.createVehicle(vehicle);
        return ResponseEntity.ok(createdVehicle);
    }
    
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleTrackingService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
}

package com.smarttransport.controller;

import com.smarttransport.dto.RouteRecommendationDto;
import com.smarttransport.service.RouteOptimizationService;
import com.smarttransport.service.CrowdPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {
    
    @Autowired
    private RouteOptimizationService routeOptimizationService;
    
    @Autowired
    private CrowdPredictionService crowdPredictionService;
    
    @GetMapping("/recommendations")
    public ResponseEntity<List<RouteRecommendationDto>> getRouteRecommendations(
            @RequestParam String from,
            @RequestParam String to) {
        
        List<RouteRecommendationDto> recommendations = 
            routeOptimizationService.getRouteRecommendations(from, to);
        
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/{routeId}/crowd-prediction")
    public ResponseEntity<Map<String, Double>> getCrowdPrediction(
            @PathVariable String routeId,
            @RequestParam int hour,
            @RequestParam int dayOfWeek) {
        
        double prediction = crowdPredictionService.predictCrowdDensity(routeId, hour, dayOfWeek);
        return ResponseEntity.ok(Map.of("crowdLevel", prediction));
    }
    
    @GetMapping("/{routeId}/statistics")
    public ResponseEntity<Map<String, Object>> getRouteStatistics(@PathVariable String routeId) {
        Map<String, Object> stats = routeOptimizationService.getRouteStatistics(routeId);
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/record-pattern")
    public ResponseEntity<String> recordTravelPattern(
            @RequestParam String routeId,
            @RequestParam String stopId,
            @RequestParam int occupancy) {
        
        crowdPredictionService.recordTravelPattern(routeId, stopId, occupancy);
        return ResponseEntity.ok("Travel pattern recorded");
    }
}

// ===== WEBSOCKET CONFIGURATION =====

package com.smarttransport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}

// ===== KAFKA CONFIGURATION =====

package com.smarttransport.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "transport-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

// ===== DATA INITIALIZATION SERVICE =====

package com.smarttransport.service;

import com.smarttransport.entity.Vehicle;
import com.smarttransport.entity.TravelPattern;
import com.smarttransport.repository.VehicleRepository;
import com.smarttransport.repository.TravelPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private TravelPatternRepository travelPatternRepository;
    
    @Autowired
    private CrowdPredictionService crowdPredictionService;
    
    @Override
    public void run(String... args) throws Exception {
        initializeVehicles();
        initializeTravelPatterns();
        crowdPredictionService.initializeMLModel();
    }
    
    private void initializeVehicles() {
        if (vehicleRepository.count() == 0) {
            Random random = new Random();
            
            // Main Line vehicles
            for (int i = 1; i <= 5; i++) {
                Vehicle vehicle = new Vehicle("ML" + String.format("%03d", i), "R001", "BUS", 50);
                vehicle.setLatitude(12.9716 + random.nextDouble() * 0.1);
                vehicle.setLongitude(77.5946 + random.nextDouble() * 0.1);
                vehicle.setCurrentOccupancy(random.nextInt(50));
                vehicleRepository.save(vehicle);
            }
            
            // Circle Line vehicles
            for (int i = 1; i <= 4; i++) {
                Vehicle vehicle = new Vehicle("CL" + String.format("%03d", i), "R002", "METRO", 80);
                vehicle.setLatitude(12.9716 + random.nextDouble() * 0.1);
                vehicle.setLongitude(77.5946 + random.nextDouble() * 0.1);
                vehicle.setCurrentOccupancy(random.nextInt(80));
                vehicleRepository.save(vehicle);
            }
            
            // Suburban vehicles
            for (int i = 1; i <= 3; i++) {
                Vehicle vehicle = new Vehicle("SB" + String.format("%03d", i), "R003", "BUS", 40);
                vehicle.setLatitude(12.9716 + random.nextDouble() * 0.1);
                vehicle.setLongitude(77.5946 + random.nextDouble() * 0.1);
                vehicle.setCurrentOccupancy(random.nextInt(40));
                vehicleRepository.save(vehicle);
            }
            
            System.out.println("Initialized vehicles in database");
        }
    }
    
    private void initializeTravelPatterns() {
        if (travelPatternRepository.count() == 0) {
            Random random = new Random();
            String[] routes = {"R001", "R002", "R003"};
            String[] stops = {"Central Station", "Business District", "University", 
                             "Shopping Mall", "Downtown", "Hospital"};
            
            // Generate historical travel patterns
            for (String route : routes) {
                for (String stop : stops) {
                    for (int hour = 6; hour <= 22; hour++) {
                        for (int day = 1; day <= 7; day++) {
                            int occupancy;
                            // Rush hours (8-10, 17-19) have higher occupancy
                            if ((hour >= 8 && hour <= 10) || (hour >= 17 && hour <= 19)) {
                                occupancy = 60 + random.nextInt(30); // 60-90%
                            } else if (hour >= 11 && hour <= 16) {
                                occupancy = 30 + random.nextInt(30); // 30-60%
                            } else {
                                occupancy = 10 + random.nextInt(20); // 10-30%
                            }
                            
                            TravelPattern pattern = new TravelPattern(route, stop, hour, day, occupancy);
                            travelPatternRepository.save(pattern);
                        }
                    }
                }
            }
            
            System.out.println("Initialized travel patterns in database");
        }
    }
}

// ===== SCHEDULED TASKS SERVICE =====

package com.smarttransport.service;

import com.smarttransport.dto.LocationUpdateDto;
import com.smarttransport.entity.Vehicle;
import com.smarttransport.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class ScheduledTasksService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private VehicleTrackingService vehicleTrackingService;
    
    private final Random random = new Random();
    
    // Simulate vehicle movement and occupancy changes every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void simulateVehicleUpdates() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        
        for (Vehicle vehicle : vehicles) {
            // Simulate small location changes
            double latChange = (random.nextDouble() - 0.5) * 0.01; // Small movement
            double lngChange = (random.nextDouble() - 0.5) * 0.01;
            
            double newLat = Math.max(12.9, Math.min(13.1, vehicle.getLatitude() + latChange));
            double newLng = Math.max(77.5, Math.min(77.7, vehicle.getLongitude() + lngChange));
            
            // Simulate occupancy changes
            int occupancyChange = random.nextInt(11) - 5; // -5 to +5
            int newOccupancy = Math.max(0, Math.min(vehicle.getCapacity(), 
                                       vehicle.getCurrentOccupancy() + occupancyChange));
            
            LocationUpdateDto update = new LocationUpdateDto(
                vehicle.getVehicleNumber(), newLat, newLng, newOccupancy);
            
            vehicleTrackingService.updateVehicleLocation(update);
        }
        
        System.out.println("Simulated updates for " + vehicles.size() + " vehicles");
    }
}

// ===== APPLICATION PROPERTIES =====

# application.yml
spring:
  application:
    name: smart-transport-system
  
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: false
  
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: transport-group
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer

server:
  port: 8080

logging:
  level:
    com.smarttransport: INFO
    org.springframework.kafka: WARN

# ===== API DOCUMENTATION CONTROLLER =====

package com.smarttransport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiDocumentationController {
    
    @GetMapping("/info")
    public Map<String, Object> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Smart Public Transport & Crowd Management System");
        info.put("version", "1.0.0");
        info.put("description", "Real-time vehicle tracking with crowd prediction and route optimization");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/vehicles", "Get all vehicles");
        endpoints.put("GET /api/vehicles/route/{routeId}", "Get vehicles by route");
        endpoints.put("POST /api/vehicles", "Create new vehicle");
        endpoints.put("POST /api/vehicles/location-update", "Update vehicle location");
        endpoints.put("GET /api/routes/recommendations", "Get route recommendations");
        endpoints.put("GET /api/routes/{routeId}/crowd-prediction", "Get crowd prediction");
        endpoints.put("GET /api/routes/{routeId}/statistics", "Get route statistics");
        endpoints.put("WebSocket /ws", "Real-time vehicle updates");
        
        info.put("endpoints", endpoints);
        
        Map<String, String> features = new HashMap<>();
        features.put("realTimeTracking", "Kafka-based real-time GPS tracking");
        features.put("crowdPrediction", "ML-based crowd density prediction using Weka");
        features.put("routeOptimization", "Smart route recommendations");
        features.put("webSocketUpdates", "Real-time updates to frontend clients");
        features.put("historicalAnalysis", "Pattern-based travel analysis");
        
        info.put("features", features);
        
        return info;
    }
}

// ===== EXAMPLE CLIENT HTML PAGE =====

// static/index.html
<!DOCTYPE html>
<html>
<head>
    <title>Smart Transport Dashboard</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .container { max-width: 1200px; margin: 0 auto; }
        .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .card { border: 1px solid #ddd; padding: 20px; border-radius: 8px; }
        .vehicle { margin: 10px 0; padding: 10px; border: 1px solid #eee; }
        .high-crowd { background-color: #ffebee; }
        .medium-crowd { background-color: #fff3e0; }
        .low-crowd { background-color: #e8f5e8; }
        button { padding: 10px 20px; margin: 5px; }
        input, select { padding: 8px; margin: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Smart Transport Dashboard</h1>
        
        <div class="grid">
            <div class="card">
                <h3>Route Recommendations</h3>
                <div>
                    <select id="fromStop">
                        <option value="Central Station">Central Station</option>
                        <option value="Business District">Business District</option>
                        <option value="University">University</option>
                        <option value="Shopping Mall">Shopping Mall</option>
                        <option value="Downtown">Downtown</option>
                    </select>
                    <select id="toStop">
                        <option value="Airport">Airport</option>
                        <option value="Hospital">Hospital</option>
                        <option value="Park View">Park View</option>
                        <option value="Stadium">Stadium</option>
                        <option value="Tech Park">Tech Park</option>
                    </select>
                    <button onclick="getRecommendations()">Get Routes</button>
                </div>
                <div id="recommendations"></div>
            </div>
            
            <div class="card">
                <h3>Live Vehicle Tracking</h3>
                <div>
                    <select id="routeSelect">
                        <option value="R001">Main Line Express</option>
                        <option value="R002">Circle Line</option>
                        <option value="R003">Suburban Express</option>
                    </select>
                    <button onclick="trackRoute()">Track Route</button>
                </div>
                <div id="vehicles"></div>
            </div>
        </div>
        
        <div class="card">
            <h3>System Status</h3>
            <div id="status">Connecting...</div>
        </div>
    </div>

    <script>
        let stompClient = null;

        function connect() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                document.getElementById('status').innerHTML = 'Connected to real-time updates';
                console.log('Connected: ' + frame);
            });
        }

        function getRecommendations() {
            const from = document.getElementById('fromStop').value;
            const to = document.getElementById('toStop').value;
            
            fetch(`/api/routes/recommendations?from=${from}&to=${to}`)
                .then(response => response.json())
                .then(data => {
                    let html = '<h4>Recommended Routes:</h4>';
                    data.forEach(route => {
                        const crowdClass = route.crowdLevel < 0.3 ? 'low-crowd' : 
                                         route.crowdLevel < 0.6 ? 'medium-crowd' : 'high-crowd';
                        html += `<div class="vehicle ${crowdClass}">
                                    <strong>${route.routeName}</strong><br>
                                    Time: ${route.estimatedTime} min | 
                                    Crowd: ${Math.round(route.crowdLevel * 100)}%<br>
                                    <small>${route.reason}</small>
                                </div>`;
                    });
                    document.getElementById('recommendations').innerHTML = html;
                });
        }

        function trackRoute() {
            const routeId = document.getElementById('routeSelect').value;
            
            fetch(`/api/vehicles/route/${routeId}`)
                .then(response => response.json())
                .then(data => {
                    let html = '<h4>Active Vehicles:</h4>';
                    data.forEach(vehicle => {
                        const occupancyRate = Math.round((vehicle.currentOccupancy / vehicle.capacity) * 100);
                        const crowdClass = occupancyRate < 30 ? 'low-crowd' : 
                                         occupancyRate < 60 ? 'medium-crowd' : 'high-crowd';
                        html += `<div class="vehicle ${crowdClass}">
                                    <strong>${vehicle.vehicleNumber}</strong> (${vehicle.vehicleType})<br>
                                    Occupancy: ${vehicle.currentOccupancy}/${vehicle.capacity} (${occupancyRate}%)<br>
                                    Location: ${vehicle.latitude.toFixed(4)}, ${vehicle.longitude.toFixed(4)}<br>
                                    <small>Updated: ${new Date(vehicle.lastUpdated).toLocaleTimeString()}</small>
                                </div>`;
                    });
                    document.getElementById('vehicles').innerHTML = html;
                });

            // Subscribe to real-time updates for this route
            if (stompClient) {
                stompClient.subscribe(`/topic/vehicles/${routeId}`, function (message) {
                    console.log('Vehicle update:', JSON.parse(message.body));
                    trackRoute(); // Refresh the display
                });
            }
        }

        // Initialize connection on page load
        connect();
    </script>
</body>
</html>

// ===== README.md =====

# Smart Public Transport & Crowd Management System

A comprehensive Java Spring Boot application that provides real-time vehicle tracking, crowd prediction, and route optimization for public transportation systems.

## Features

### 🚌 Real-time Vehicle Tracking
- GPS-based location tracking using Apache Kafka
- WebSocket support for live updates
- Vehicle occupancy monitoring

### 🤖 ML-powered Crowd Prediction
- Weka-based machine learning for crowd density prediction
- Historical travel pattern analysis
- Time-based occupancy forecasting

### 🗺️ Smart Route Optimization
- Alternative route suggestions
- Crowd-aware travel recommendations
- Real-time route statistics

### 📊 Analytics Dashboard
- Live vehicle tracking interface
- Route recommendation system
- Historical data visualization

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.1
- **Database**: H2 (in-memory for demo)
- **Message Queue**: Apache Kafka
- **ML Framework**: Weka (Java ML library)
- **Real-time Communication**: WebSocket, STOMP
- **API Integration**: Google Maps API ready
- **Build Tool**: Maven

## Quick Start

### Prerequisites
- Java 17+
- Apache Kafka (for production)
- Maven 3.6+

### Installation

1. Clone and build the project:
```bash
git clone <repository-url>
cd smart-transport-system
mvn clean install
```

2. Start Kafka (for production):
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties

# Create topic
bin/kafka-topics.sh --create --topic vehicle-locations --bootstrap-server localhost:9092
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Access the application:
- API: http://localhost:8080/api/info
- Dashboard: http://localhost:8080/index.html
- H2 Console: http://localhost:8080/h2-console

## API Endpoints

### Vehicle Management
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/route/{routeId}` - Get vehicles by route
- `POST /api/vehicles` - Create new vehicle
- `POST /api/vehicles/location-update` - Update vehicle location

### Route Optimization
- `GET /api/routes/recommendations?from={stop}&to={stop}` - Get route recommendations
- `GET /api/routes/{routeId}/crowd-prediction?hour={h}&dayOfWeek={d}` - Crowd prediction
- `GET /api/routes/{routeId}/statistics` - Route statistics

### Real-time Updates
- WebSocket endpoint: `/ws`
- Topic: `/topic/vehicles/{routeId}`

## Sample API Usage

### Update Vehicle Location
```bash
curl -X POST http://localhost:8080/api/vehicles/location-update \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleNumber": "ML001",
    "latitude": 12.9716,
    "longitude": 77.5946,
    "currentOccupancy": 25
  }'
```

### Get Route Recommendations
```bash
curl "http://localhost:8080/api/routes/recommendations?from=Central%20Station&to=Airport"
```

## Architecture

### Data Flow
1. **GPS Devices** → Kafka Topic (`vehicle-locations`)
2. **Kafka Consumer** → Database Update + WebSocket Broadcast
3. **ML Service** → Crowd Prediction based on historical patterns
4. **Route Service** → Optimization using current + predicted data
5. **Frontend** → Real-time updates via WebSocket

### Components
- **VehicleTrackingService**: Handles GPS updates and real-time processing
- **CrowdPredictionService**: ML-based crowd density forecasting
- **RouteOptimizationService**: Smart route recommendations
- **WebSocket**: Real-time client updates

## Configuration

### Application Properties (application.yml)
- Database: H2 in-memory (configurable to PostgreSQL/MySQL)
- Kafka: localhost:9092 (configurable)
- Server: Port 8080

### Mock Data
- 12 vehicles across 3 routes
- Historical travel patterns for ML training
- Simulated real-time updates every 30 seconds

## Production Deployment

### Database Migration
Replace H2 with production database:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/transport_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### External Services Integration
- Google Maps API for route planning
- Real GPS devices for location updates
- Production Kafka cluster

### Scaling Considerations
- Kafka partitioning for high-throughput vehicle updates
- Database indexing for optimal query performance
- Caching for frequently accessed route data
- Load balancing for multiple service instances

## License
MIT License - See LICENSE file for details