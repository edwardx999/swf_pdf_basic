package acceptance;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.junit.Test;

import com.flagstone.transform.Background;
import com.flagstone.transform.DefineVideo;
import com.flagstone.transform.Movie;
import com.flagstone.transform.Place2;
import com.flagstone.transform.ShowFrame;
import com.flagstone.transform.Strings;
import com.flagstone.transform.VideoFrame;
import com.flagstone.transform.video.Deblocking;
import com.flagstone.transform.coder.ImageTag;
import com.flagstone.transform.coder.MovieTag;
import com.flagstone.transform.datatype.Bounds;
import com.flagstone.transform.datatype.VideoFormat;
import com.flagstone.transform.datatype.WebPalette;
import com.flagstone.transform.util.image.ImageDecoder;
import com.flagstone.transform.util.image.ImageFactory;
import com.flagstone.transform.util.image.ImageInfo;
import com.flagstone.transform.util.image.ImageRegistry;
import com.flagstone.transform.video.ImageBlock;
import com.flagstone.transform.video.ScreenPacket;

public final class ScreenVideoTest {
    
    @Test
    public void showPNG() throws IOException, DataFormatException {
        
        final File sourceDir = new File("test/data/png-screenshots");
        final File destDir = new File("test/results/acceptance/ScreenVideoTest");

        final FilenameFilter filter = new FilenameFilter() {
            public boolean accept(final File directory, final String name) {
                return name.endsWith(".png");
            }
        };

        final String[] files = sourceDir.list(filter);

        Movie movie = new Movie();

        File destFile = null;

        final int blockWidth = 64;
        final int blockHeight = 64;

        final int numberOfFrames = files.length;
        final Deblocking deblocking = Deblocking.OFF;
        final boolean smoothing = false;
        final VideoFormat codec = VideoFormat.SCREEN;
        int identifier;

        final ImageFactory factory = new ImageFactory();
        factory.read(new File(files[0]));
        final ImageTag image = factory.defineImage(movie.identifier());

        int screenWidth = image.getWidth();
        int screenHeight = image.getHeight();

        movie = new Movie();
        identifier = movie.identifier();

        movie.setFrameSize(new Bounds(0, 0, screenWidth * 20,
                        screenHeight * 20));
        movie.setFrameRate(4.0f);
        movie.add(new Background(WebPalette.ALICE_BLUE.color()));

        movie.add(new DefineVideo(identifier, numberOfFrames, screenWidth,
                screenHeight, deblocking, smoothing, codec));

        final List<ImageBlock> prev = new ArrayList<ImageBlock>();
        final List<ImageBlock> next = new ArrayList<ImageBlock>();
        List<ImageBlock> delta = new ArrayList<ImageBlock>();
        
        factory.getImageAsBlocks(prev, blockWidth, blockHeight);

        ScreenPacket packet = new ScreenPacket(true, screenWidth, screenHeight,
                blockWidth, blockHeight, prev);

        movie.add(Place2.show(identifier, 1, 0, 0));
        movie.add(new VideoFrame(identifier, 0, packet.encode()));
        movie.add(ShowFrame.getInstance());

        Place2 place;

        for (int i = 1; i < numberOfFrames; i++) {
            final File srcFile = new File(sourceDir, files[i]);

            factory.read(srcFile);
            factory.getImageAsBlocks(next, blockWidth, blockHeight);

            delta = new ArrayList<ImageBlock>(prev.size());

            for (int j = 0; j < prev.size(); j++) {
                if (prev.get(j).equals(next.get(j))) {
                    delta.add(new ImageBlock(0, 0, null));
                } else {
                    delta.add(next.get(j));
                }
            }

            packet = new ScreenPacket(false, screenWidth, screenHeight,
                    blockWidth, blockHeight, delta);
            place = new Place2().move(1, 0, 0);
            place.setRatio(i);

            movie.add(place);
            movie.add(new VideoFrame(identifier, i, packet.encode()));
            movie.add(ShowFrame.getInstance());
        }

        destFile = new File(destDir, sourceDir.getName() + ".swf");
        movie.encodeToFile(destFile);
    }
}
