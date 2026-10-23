package com.wagerpro.catalogservice.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wagerpro.catalogservice.infrastructure.persistence.entity.Match;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchOddsSummaryProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchDetailsProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.UpcomingMatchesProjection;

public interface MatchJpaRepository extends JpaRepository<Match, Long> {
	
	@Query(value = """
            SELECT o.id, md.type, o.odd_value 
            FROM odds o
            JOIN market_definitions md on md.id = o.market_definition_id
            WHERE o.match_id = :matchId AND o.is_active = true
            """, nativeQuery = true)
//	join matches m on m.id = odd.match_id
    List<MatchOddsSummaryProjection> findOddsByMatchId(@Param("matchId") Long matchId);
    
	@Query(value = """
		    SELECT 
		        m.id,
		        l.country,
		        l.name as leagueName,
		        m.home_team,
		        m.away_team,
		        m.start_time,
		        md.type as type,
		        o.odd_value
		    FROM matches m
		    JOIN leagues l ON l.id = m.league_id
		    JOIN odds o ON o.match_id = m.id AND o.is_active = true
		    JOIN market_definitions md ON md.id = o.market_definition_id
		    WHERE m.id IN :matchIds
		      AND (md.type IN ('FT_1', 'FT_X', 'FT_2') OR md.type IS NULL)
		    ORDER BY m.start_time ASC
		""", nativeQuery = true)
    List<UpcomingMatchesProjection> findBasicOddsForMatchIds(@Param("matchIds") List<Long> matchIds);
    
    List<Match> findByStatusAndStartTimeLessThanEqual(String status, LocalDateTime time);

    
	@Query(value = """
	    select m.id from matches m
		join leagues l on l.id = m.league_id
		join sports s on s.id = l.sport_id
		WHERE TRIM(m.status) = TRIM('UPCOMING') AND TRIM(s.name) = TRIM(:sportName)
	    ORDER BY m.start_time ASC
	""", nativeQuery = true)
	List<Long> findUpcomingMatchIdsBySport(@Param("sportName") String sportName);
	
	
	@Query("""
		    select m.id as matchId, 
		           s.name as sportName, 
		           l.name as leagueName, 
		           m.homeTeam as homeTeam, 
		           m.awayTeam as awayTeam, 
		           m.startTime as startTime, 
		           m.status as status
		    from Match m 
		    join m.league l 
		    join l.sport s 
		    where m.id in :matchIds
		    """)
	List<MatchDetailsProjection> findDetailsForTicket(@Param("matchIds") List<Long> matchIds);
	
	
}
