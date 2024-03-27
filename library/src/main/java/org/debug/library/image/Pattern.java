package org.debug.library.image;

import lombok.Getter;
import lombok.Setter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.debug.library.location.Anchor;
import org.debug.library.location.Anchors;
import org.debug.library.location.Position;
import org.debug.library.location.Positions;
import org.debug.library.region.Region;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Pattern and StateImage_ (to replace StateImage) are for a code restructuring that gets rid of RegionImagePairs.
 * This can also replace DynamicImage, which currently only uses KmeansProfiles for one of its Image objects.
 * StateImage, which can hold multiple Pattern objects, is currently used for classification. Pattern could also be used
 * but not all Pattern objects are associated with StateImage objects and some are generic (without an image).
 *
 * TODO: If Pattern is never used for classification, you can remove the KMeansProfiles.
 *
 * <p>Database: Pattern is an @Entity since a single Pattern may be in multiple StateImage objects. All fields in
 * Pattern should be @Embedded. This is the lowest level of object that should be stored in the database.
 * I removed the SikuliX Pattern since it is a non-JPA entity and caused problems with persistence. </p>
 */
@Getter
@Setter
public class Pattern {

    // fields from SikuliX Pattern
    private String url; // originally type URL, which requires conversion for use with JPA
    private String imgpath;

    private String name;
    /*
    An image that should always appear in the same location has fixed==true.
    */
    private boolean fixed = false;
    private boolean setKmeansColorProfiles = false; // this is an expensive operation and should be done only when needed
    //@Embedded
    //private KmeansProfilesAllSchemas kmeansProfilesAllSchemas = new KmeansProfilesAllSchemas();
    //@Embedded
    //private ColorCluster colorCluster = new ColorCluster();
    private int index; // a unique identifier used for classification matrices
    private boolean dynamic = false; // dynamic images cannot be found using pattern matching
    private Position position = new Position(.5,.5); // use to convert a match to a location
    private Anchors anchors = new Anchors(); // for defining regions using this object as input

    /*
    The SikuliX Image object is part of Pattern. It loads an image from file or the web into memory
    and provides a BufferedImage object. There are, however, occasions requiring an image to be
    captured only in memory. For example, when Brobot creates a model of the environment, the images
    captured can be saved in a database and do not need to be on file. This could be achieved with a
    SikuliX Image by setting the BufferedImage directly `super(bimg)`.

    It would be possible to use the SikuliX Image within Pattern for working with the physical representation of
    an image, but this raises architecture concerns in Brobot. Most importantly, Match needs a representation of the
    image, and having Pattern within Match would create a circular class chain. Pattern also needs a MatchHistory to
    perform mock runs and MatchHistory contains a list of MatchSnapshot, which itself contains a list of Match objects.
    For this reason, I've created a Brobot Image object and use this object in Match and in Pattern.
     */
    private Image image;

    public Pattern(BufferedImage bimg) {
        image = new Image(bimg);
    }

    public Pattern(Image image) {
        this.image = image;
        setName(image.getName());
    }

    /**
     * Creates a generic Pattern without an associated image.
     */
    public Pattern() {}

    /**
     * Converts the BufferedImage in Image to a BGR JavaCV Mat.
     * @return a BGR Mat.
     */

    /**
     * Setting the Mat involves converting the BGR Mat parameter to a BufferedImage and saving it in the SukiliX Image.
     * Mat objects are not stored explicitly in Pattern but converted to and from BufferedImage objects.
     * @param matBGR a JavaCV Mat in BGR format.
     */

    /**
     * Converts the BufferedImage in Image to an HSV JavaCV Mat.
     * @return an HSV Mat.
     */

    private void setNameFromFilenameIfEmpty(String filename) {
        if (filename == null) return;
        if (name == null || name.isEmpty()) {
            File file = new File(filename); // Create a File object from the image path
            setName(file.getName().replaceFirst("[.][^.]+$", "")); // the file name without extension
        }
    }

    public int w() {
        if (image == null) return 0;
        return image.w();
    }

    public int h() {
        if (image == null) return 0;
        return image.h();
    }

    /**
     * If the image has a fixed location and has already been found, the region where it was found is returned.
     * If there are multiple regions, this returns a random selection.
     * @return a region
     */

    /**
     * If the image has a fixed location and has already been found, the region where it was found is returned.
     * Otherwise, all regions are returned.
     * @return all usable regions
     */

    public int size() {
        return w() * h();
    }

    public boolean isEmpty() {
        return image.isEmpty();
    }

    public boolean equals(Pattern comparePattern) {
        boolean sameFilename = imgpath.equals(comparePattern.getImgpath());
        boolean bothFixedOrBothNot = fixed == comparePattern.isFixed();
        boolean bothDynamicOrBothNot = dynamic == comparePattern.isDynamic();
        boolean samePosition = position.equals(comparePattern.getPosition());
        boolean sameAnchors = anchors.equals(comparePattern.getAnchors());
        if (!sameFilename) return false;
        if (!bothFixedOrBothNot) return false;
        if (!bothDynamicOrBothNot) return false;
        if (!samePosition) return false;
        if (!sameAnchors) return false;
        return true;
    }

    // __convenience functions for the SikuliX Pattern object__
    /**
     * Another way to get the SikuliX object.
     * @return the SikuliX Pattern object.
     */
    public org.sikuli.script.Pattern sikuli() {
        return new org.sikuli.script.Pattern(image.sikuli());
    }

    public BufferedImage getBImage() {
        return image.getBufferedImage();
    }

    public static class Builder {
        private String name;
        private Image image;
        private BufferedImage bufferedImage;
        private String filename;
        private boolean fixed = false;
        private boolean setKmeansColorProfiles = false;
        private int index;
        private boolean dynamic = false;
        private Position position = new Position(.5,.5);
        private Anchors anchors = new Anchors();

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setImage(Image image) {
            this.image = image;
            return this;
        }

        public Builder setBufferedImage(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
            return this;
        }

        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder setFixed(boolean isFixed) {
            this.fixed = isFixed;
            return this;
        }

        public Builder setSetKmeansColorProfiles(boolean setKmeansColorProfiles) {
            this.setKmeansColorProfiles = setKmeansColorProfiles;
            return this;
        }

        //public Builder setKmeansProfilesAllSchemas(KmeansProfilesAllSchemas kmeansProfilesAllSchemas) {
        //    this.kmeansProfilesAllSchemas = kmeansProfilesAllSchemas;
        //    return this;
        //}

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setDynamic(boolean isDynamic) {
            this.dynamic = isDynamic;
            return this;
        }

        public Builder setPosition(Position position) {
            this.position = position;
            return this;
        }

        public Builder setAnchors(Anchors anchors) {
            this.anchors = anchors;
            return this;
        }

        public Builder addAnchor(Anchor anchor) {
            this.anchors.add(anchor);
            return this;
        }

        private Pattern makeNewPattern() {
            if (filename != null) return new Pattern();
            if (bufferedImage != null) return new Pattern(bufferedImage);
            return new Pattern();
        }

        private void createAndSetImage(Pattern pattern) {
            if (image != null) pattern.setImage(image);
            if (bufferedImage == null) return;
            if (pattern.getImage() != null) pattern.getImage().setBufferedImage(bufferedImage);
            else pattern.setImage(new Image(bufferedImage, pattern.getName()));
        }

        public Pattern build() {
            Pattern pattern = makeNewPattern();
            if (name != null) pattern.setName(name);
            createAndSetImage(pattern);
            pattern.setFixed(fixed);
            pattern.setSetKmeansColorProfiles(setKmeansColorProfiles);
            pattern.setIndex(index);
            pattern.setDynamic(dynamic);
            pattern.setPosition(position);
            pattern.setAnchors(anchors);
            return pattern;
        }
    }

}
