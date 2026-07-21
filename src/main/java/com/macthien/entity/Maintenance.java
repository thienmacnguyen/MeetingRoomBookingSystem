package entity;

import enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Long maintenanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private MeetingRoom room;

    @Column(name = "maintenance_start_time", nullable = false)
    private LocalDateTime maintenanceStartTime;

    @Column(name = "maintenance_end_time", nullable = false)
    private LocalDateTime maintenanceEndTime;

    @Column(name = "maintenance_reason", columnDefinition = "TEXT")
    private String maintenanceReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_status", nullable = false, length = 30)
    private MaintenanceStatus maintenanceStatus;
}
