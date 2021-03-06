package featurescomparison.workingwithpresentations.helloworld.java;

import java.net.URI;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.contenttype.ContentTypeManager;
import org.docx4j.openpackaging.contenttype.ContentTypes;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.PresentationML.MainPresentationPart;
import org.docx4j.openpackaging.parts.PresentationML.SlideLayoutPart;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
import org.pptx4j.jaxb.Context;
import org.pptx4j.pml.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pptx4jHelloWorld {

	protected static Logger log = LoggerFactory.getLogger(Pptx4jHelloWorld.class);
	
	private static boolean MACRO_ENABLE = true;
		
	public static void main(String[] args) throws Exception {

		String dataPath = "src/featurescomparison/workingwithpresentations/helloworld/data/";
		
		// Where will we save our new .ppxt?
		String outputfilepath = dataPath + "Pptx4jHelloWorld.pptx";
		if (MACRO_ENABLE) outputfilepath += "m";
		
		// Create skeletal package, including a MainPresentationPart and a SlideLayoutPart
		PresentationMLPackage presentationMLPackage = PresentationMLPackage.createPackage(); 
		
		if (MACRO_ENABLE) {
			ContentTypeManager ctm = presentationMLPackage.getContentTypeManager();
			ctm.removeContentType(new PartName("/ppt/presentation.xml") );
			ctm.addOverrideContentType(new URI("/ppt/presentation.xml"), ContentTypes.PRESENTATIONML_MACROENABLED);
		}
		 
		// Need references to these parts to create a slide
		// Please note that these parts *already exist* - they are
		// created by createPackage() above.  See that method
		// for instruction on how to create and add a part.
		MainPresentationPart pp = (MainPresentationPart)presentationMLPackage.getParts().getParts().get(
				new PartName("/ppt/presentation.xml"));		
		SlideLayoutPart layoutPart = (SlideLayoutPart)presentationMLPackage.getParts().getParts().get(
				new PartName("/ppt/slideLayouts/slideLayout1.xml"));
		
		// OK, now we can create a slide
		SlidePart slidePart = new SlidePart(new PartName("/ppt/slides/slide1.xml"));
		slidePart.setContents( SlidePart.createSld() );		
		pp.addSlide(0, slidePart);
		
		// Slide layout part
		slidePart.addTargetPart(layoutPart);
		
				
		// Create and add shape
		Shape sample = ((Shape)XmlUtils.unmarshalString(SAMPLE_SHAPE, Context.jcPML) );
		slidePart.getContents().getCSld().getSpTree().getSpOrGrpSpOrGraphicFrame().add(sample);
		
		// All done: save it
		presentationMLPackage.save(new java.io.File(outputfilepath));

		System.out.println("\n\n done .. saved " + outputfilepath);
		
	}	
	
	
	private static String SAMPLE_SHAPE = 			
		"<p:sp   xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
		+ "<p:nvSpPr>"
		+ "<p:cNvPr id=\"4\" name=\"Title 3\" />"
		+ "<p:cNvSpPr>"
			+ "<a:spLocks noGrp=\"1\" />"
		+ "</p:cNvSpPr>"
		+ "<p:nvPr>"
			+ "<p:ph type=\"title\" />"
		+ "</p:nvPr>"
	+ "</p:nvSpPr>"
	+ "<p:spPr />"
	+ "<p:txBody>"
		+ "<a:bodyPr />"
		+ "<a:lstStyle />"
		+ "<a:p>"
			+ "<a:r>"
				+ "<a:rPr lang=\"en-US\" smtClean=\"0\" />"
				+ "<a:t>Hello World</a:t>"
			+ "</a:r>"
			+ "<a:endParaRPr lang=\"en-US\" />"
		+ "</a:p>"
	+ "</p:txBody>"
+ "</p:sp>";

}
