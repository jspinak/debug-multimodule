package org.debug.library.location;

import lombok.Getter;

/**
 * Position is defined by (%w, %h) and can be used to get a location in a Region or Match.
 * The SikuliX class has offset.x and offset.y, which
 *    point to a location as an offset from the match's center. This is ok when you know the size of the
 *    image but less convenient for working with general areas of an image (i.e. top left, bottom right).
 */
@Getter
public class Position {

    private double percentW;
    private double percentH;

    public Position() {
        this.percentW = 0.5;
        this.percentH = 0.5;
    }

    public Position(double w, double h) {
        this.percentW = w;
        this.percentH = h;
    }

    public Position(int percentW, int percentH) {
        this.percentW = (double) percentW / 100;
        this.percentH = (double) percentH / 100;
    }

    public void addPercentW(double addW) {
        percentW += addW;
    }

    public void addPercentH(double addH) {
        percentH += addH;
    }

    public void multiplyPercentW(double mult) {
        percentW = percentW * mult;
    }

    public void multiplyPercentH(double mult) {
        percentH = percentH * mult;
    }

    @Override
    public String toString() {
        return String.format("P[%.1f.%.1f]", percentW, percentH);
    }

    public boolean equals(Position position) {
        return percentW == position.getPercentW() && percentH == position.getPercentH();
    }

}
