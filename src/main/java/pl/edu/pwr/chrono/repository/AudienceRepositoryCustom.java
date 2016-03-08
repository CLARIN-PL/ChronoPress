package pl.edu.pwr.chrono.repository;

import java.util.Set;

public interface AudienceRepositoryCustom {

    Set<String> findAudienceJournalTitles(Set<String> audience);
}
