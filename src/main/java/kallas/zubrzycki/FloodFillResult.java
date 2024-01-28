package kallas.zubrzycki;

import java.util.Set;

public class FloodFillResult {
    Set<EPointColor> surroundingColors;
    int count;

    public FloodFillResult(Set<EPointColor> surroundingColors, int count) {
        this.surroundingColors = surroundingColors;
        this.count = count;
    }
}
