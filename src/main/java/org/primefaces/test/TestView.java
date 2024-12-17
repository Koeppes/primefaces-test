package org.primefaces.test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

import lombok.Data;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Data
@Named
@SessionScoped
public class TestView implements Serializable {
    private static final String DIALOG_WV = "myDlgWV";
    private String string;
    private Integer integer;
    private BigDecimal decimal;
    private LocalDateTime localDateTime;
    private List<TestObject> list;
    private StreamedContent file;

    private StreamedContent graphicText;

    public void showPrintDialog() {
        PrimeFaces.current().executeScript("PF('calendarPrintDialogWV').show();");
    }

    public void showPrintDialogDynamic() {
        PrimeFaces.current().executeScript("PF('calendarPrintDialogDynamicWV').show();");
    }

    public StreamedContent getGraphicText() {

        try {
            graphicText = DefaultStreamedContent.builder()
                    .contentType("image/png")
                    .stream(() -> {
                        try {
                            BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = bufferedImg.createGraphics();
                            g2.drawString("This is a text", 0, 10);
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ImageIO.write(bufferedImg, "png", os);
                            return new ByteArrayInputStream(os.toByteArray());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return graphicText;
    }
    @PostConstruct
    public void init() {
        string = "Welcome to PrimeFaces!!!";
        list = new ArrayList<>(Arrays.asList(
                new TestObject("Thriller", "Michael Jackson", 1982),
                new TestObject("Back in Black", "AC/DC", 1980),
                new TestObject("The Bodyguard", "Whitney Houston", 1992),
                new TestObject("The Dark Side of the Moon", "Pink Floyd", 1973)
        ));
    }

    public void showDialog() {
        PrimeFaces.current().executeScript("PF('" + DIALOG_WV + "').show();");
    }

    public void hideDialog() {
        PrimeFaces.current().executeScript("PF('" + DIALOG_WV + "').hide();");
    }

    public StreamedContent download() {
        StreamedContent result = null;
        result =  DefaultStreamedContent.builder()
                .name("downloaded_boromir.jpg")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/boromir.jpg"))
                .build();
        return result;
    }

}
