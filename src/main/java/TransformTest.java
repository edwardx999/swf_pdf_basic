import com.flagstone.transform.*;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.flagstone.transform.fillstyle.FillStyle;
import com.flagstone.transform.fillstyle.SolidFill;
import com.flagstone.transform.font.DefineFont;
import com.flagstone.transform.font.DefineFont2;
import com.flagstone.transform.linestyle.LineStyle;
import com.flagstone.transform.shape.*;
import com.flagstone.transform.text.DefineText2;
import org.apache.pdfbox.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDPattern;


public class TransformTest {

    private static PDFont convertFont(DefineFont2 font) {
       return null;
    }

    private static void drawShape(DefineShape3 shape, PDDocument doc, PDPage page, float ox, float oy) throws IOException {
        var fillIter=shape.getFillStyles().iterator();
        var lineIter=shape.getLineStyles().iterator();

        var shapeIter=shape.getShape().getObjects().iterator();
        var painter=new PDPageContentStream(doc, page);
        boolean fill=false;
        boolean stroke=false;
        while(shapeIter.hasNext()){
            var record=shapeIter.next();
            if(record instanceof Curve){
                var curve=(Curve)record;
            }
            else if (record instanceof Line){
                var line = (Line)record;
                painter.lineTo(fromTwit(line.getX()),fromTwit(line.getY()));
            }
            else if(record instanceof ShapeStyle){
                var style=(ShapeStyle) record;
                if(style.getMoveX()!=null&&style.getMoveY()!=null){
                    painter.moveTo(fromTwit(style.getMoveX()),fromTwit(style.getMoveY()));
                }
                else if(style.getFillStyle()!=null){
                    fill=style.getFillStyle()=1;
                }
                else if(style.getLineStyle()!=null){
                    stroke=
                }
            }
        }
    }

    private static void drawText(DefineText2 text, PDFont font, PDDocument doc, PDPage page, float ox, float oy){

    }

    public static final float TWIT_CONVERSION=20.0f;

    private static float fromTwit(int twit){
        return twit/TWIT_CONVERSION;
    }

    public static void main(String[] args){
        try{
            var movie=new Movie();
            movie.decodeFromFile(new File("5.swf"));
            var objs=movie.getObjects();
            for(var obj:objs) {
                System.out.println(obj.toString());
            }
            var fonts= new HashMap<Integer, PDFont>();
            var shapes=new HashMap<Integer, DefineShape3>();
            var doc=new PDDocument();
            PDPage page=null;

            for(var obj:objs) {
                if(obj instanceof MovieHeader){
                    var header=(MovieHeader)obj;
                    page=new PDPage(
                            new PDRectangle(
                                    fromTwit(header.getFrameSize().getWidth()),
                                    fromTwit(header.getFrameSize().getHeight())));
                    page.setResources(new PDResources());
                    doc.addPage(page);
                }
                if(obj instanceof DefineFont2){
                    var font=(DefineFont2) obj;
                    fonts.put(font.getIdentifier(), convertFont(font));
                }
                else if(obj instanceof Place2){
                    var place=(Place2) obj;
                    drawShape(shapes.get(place.getIdentifier()), page);
                }
                else if (obj instanceof DefineShape3){
                    var shape=(DefineShape3)obj;
                    shapes.put(shape.getIdentifier(), shape);
                }
                else if (obj instanceof DefineText2){
                    var text=(DefineText2)obj;
                    drawText(text, fonts.get(text.getIdentifier()), page);
                }
            }
        }
        catch(Throwable err) {
            System.out.println(err.getMessage());
            System.exit(1);
        }
    }
}
