package com.wagerpro.wagerservice.infrastructure.persistence.entity;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ticket_items", indexes = {
	    @Index(name = "idx_ticket_items_ticket", columnList = "ticket_id")
	})
public class TicketItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "odd_id", nullable = false)
    private Long oddId;

    @Column(name = "odd_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal oddValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_type", nullable = false, length = 50)
    private MarketType marketType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 50)
    private TicketItemStatus status = TicketItemStatus.PENDING;

}
