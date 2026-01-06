package org.mtg.tierlist;

import java.util.List;

public interface JumpstartEnvironment {
    List<JumpstartSet> sets();

    default List<JumpstartBooster> boosters() {
        return sets().stream().flatMap(s -> s.boosters().stream()).toList();
    }
}
