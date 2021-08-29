import java.util.*;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public static Integer FEET_PER_LON = 288200;

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        // Basic Check

        if (params.get("ullon") > MapServer.ROOT_LRLON
                || params.get("lrlon") < MapServer.ROOT_ULLON
                || params.get("lrlat") > MapServer.ROOT_ULLAT
                || params.get("ullat") < MapServer.ROOT_LRLAT) {
                        System.out.println("Some issue with the lat/lon");
            results.put("query_success", false);
            return results;
        }

        int depth = getDepth(params);

        Double ullon = Math.max(params.get("ullon"), MapServer.ROOT_ULLON);
        Double ullat = Math.min(params.get("ullat"), MapServer.ROOT_ULLAT);;
        Double lrlon = Math.min(params.get("lrlon"), MapServer.ROOT_LRLON);;
        Double lrlat = Math.max(params.get("lrlat"), MapServer.ROOT_LRLAT);;

        int[] topLeftFile = getLocation(ullon, ullat, depth);
        int[] bottomRightFile = getLocation(lrlon, lrlat, depth);

        int numOfFilesX = bottomRightFile[0] - topLeftFile[0] + 1;
        int numOfFilesY = bottomRightFile[1] - topLeftFile[1] + 1;

        String[][] renderGrid = new String[numOfFilesY][numOfFilesX];
        for(int x = 0; x < numOfFilesX; x++) {
            for(int y = 0; y < numOfFilesY; y++) {
                renderGrid[y][x] = "d" + depth
                        + "_x" + (topLeftFile[0] + x)
                        + "_y" + (topLeftFile[1] + y) + ".png";
            }
        }

        Double LON_DIST = getLonDist(depth);
        Double LAT_DIST = getLatDist(depth);

        results.put("depth", depth); // depth of zoom
        results.put("render_grid", renderGrid); // Grid
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + LON_DIST * topLeftFile[0]); // Upper Left longitude
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + LON_DIST * (bottomRightFile[0] + 1)); // Lower Right longitude
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT - LAT_DIST * topLeftFile[1]); // Upper Left Latitude
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT - LAT_DIST * (bottomRightFile[1] + 1)); // Lower Right Latitude
        results.put("query_success", true);

        // printGrid(renderGrid);
        System.out.println(results);

        return results;
    }

    private static int getDepth (Map<String, Double> params) {
        Double lrlon = params.get("lrlon");
        Double ullon = params.get("ullon");
        Double w = params.get("w");

        Double totalLong = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
        Double feetPerPixel = (lrlon - ullon) / w;

        for (int d = 0; d <= 7; d++) {
            if (totalLong / MapServer.TILE_SIZE / Math.pow(2, d) < feetPerPixel) {
                return d;
            }
        }

        return 7;
    }

    private static int[] getLocation(Double x, Double y, int depth) {
        int[] fileLoc = new int[2];
        final double eps = 1E-10;

        Double LON_DIST = getLonDist(depth);
        Double LAT_DIST = getLatDist(depth);

        fileLoc[0] = (int) ((x - MapServer.ROOT_ULLON - eps) / LON_DIST);
        fileLoc[1] = (int) ((MapServer.ROOT_ULLAT - y- eps) / LAT_DIST);

        return fileLoc;
    }

    private static Double getLonDist(int depth) {
        return (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
    }

    private static Double getLatDist(int depth) {
        return (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
    }

    private static void printGrid(String[][] a) {
        for (String[] b : a) {
            for (String c : b) {
                System.out.println(c);
            }
        }
    }

    public static void main(String[] args) {

        Map<String, Double> a = new HashMap<>();
        a.put("lrlon", -122.24053369025242);
        a.put("ullon", -122.24163047377972);
        a.put("w", 892.0);

        System.out.println(getDepth(a));
        System.out.println(getLocation(-122.24163047377972, 37.87655856892288, getDepth(a))[0]);
        System.out.println(getLocation(-122.24163047377972, 37.87655856892288, getDepth(a))[1]);
        System.out.println(getLocation(-122.24053369025242, 37.87548268822065, getDepth(a))[0]);
        System.out.println(getLocation(-122.24053369025242, 37.87548268822065, getDepth(a))[1]);
    }
}
