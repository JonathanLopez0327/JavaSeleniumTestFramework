package visual;

import base.BaseTest;
import base.document.CompareMode;
import base.document.PDFUtil;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class VisualTest extends BaseTest {

    private String file1 = "src/main/resources/ContratosdelProducto-PabloNicacio.pdf";
//    private String file2 = "src/main/resources/ContratosdelProducto-PabloNicacio.pdf";
    private String file2 = "src/main/resources/ContratosdelProducto-JonathanLopez.pdf";

    @Test(description = "Visual document test")
    void testVisual() throws IOException {
        PDFUtil pdfUtil = new PDFUtil();
        pdfUtil.setCompareMode(CompareMode.VISUAL_MODE);
        pdfUtil.enableLog();
        pdfUtil.setImageDestinationPath("src/main/resources/");
        pdfUtil.highlightPdfDifference(true);
        pdfUtil.compareAllPages(true);
        Assert.assertTrue(pdfUtil.compare(file1, file2, 1, 16));

    }
}
