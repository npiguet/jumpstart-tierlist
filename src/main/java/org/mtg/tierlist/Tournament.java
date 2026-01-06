package org.mtg.tierlist;

import java.io.IOException;
import java.util.List;

public interface Tournament {

    JumpstartGameOutcome playAndRecordNextMatch() throws IOException;

    List<JumpstartGameRecord> getRecords();
}
