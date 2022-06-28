package edu.rsatu.garage;

import edu.rsatu.garage.controller.RentController;
import edu.rsatu.garage.entities.Client;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocsHelper {

    public static void generateReceipt(long receiptNumber, String carNumber,
                                       Client client, double sumPrice, int boxId,
                                       LocalDate startDate, LocalDate endDate) throws IOException {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph requisites1 = document.createParagraph();
        requisites1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun requisites1Run = requisites1.createRun();
        requisites1Run.setText("ООО \"Гаражная фирма\"");
        requisites1Run.setFontFamily("Arial");
        requisites1Run.setFontSize(12);
        XWPFParagraph requisites2 = document.createParagraph();
        requisites1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun requisites2Run = requisites2.createRun();
        requisites2Run.setText("152934, г. Рыбинск, ул.Пушкина, д. 53");
        requisites2Run.setFontFamily("Arial");
        requisites2Run.setFontSize(12);
        XWPFParagraph requisites3 = document.createParagraph();
        requisites1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun requisites3Run = requisites3.createRun();
        requisites3Run.setText("ИНН 7615773259");
        requisites3Run.setFontFamily("Arial");
        requisites3Run.setFontSize(12);

        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        String textData = "Квитанция № " + receiptNumber;
        XWPFRun titleRun = title.createRun();
        titleRun.setFontFamily("Arial");
        titleRun.setFontSize(20);
        titleRun.setBold(true);
        titleRun.addBreak();
        titleRun.setText(textData);

        XWPFParagraph object = document.createParagraph();
        object.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun objectRun = object.createRun();
        objectRun.setFontFamily("Arial");
        objectRun.setFontSize(12);
        objectRun.setText("на оплату услуг аренды");

        List<String> labels1 = List.of("Арендатор", "Адрес", "Государственный номер автомобиля");
        List<String> values1 = new ArrayList<>();
        values1.add(client.getSurname());
        values1.add(client.getAddress());
        values1.add(carNumber);
        XWPFTable clientTable = document.createTable(labels1.size(), 2);
        clientTable.setWidthType(TableWidthType.PCT);
        clientTable.setWidth("100%");
        clientTable.setTopBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        clientTable.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "000000");
        clientTable.setLeftBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        clientTable.setRightBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        clientTable.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "000000");
        clientTable.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        for (int i = 0; i < labels1.size(); i++) {
            XWPFTableCell cell1 = clientTable.getRow(i).getCell(0);
            XWPFParagraph label = cell1.addParagraph();
            label.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun surnameLabelRun = label.createRun();
            surnameLabelRun.setFontFamily("Arial");
            surnameLabelRun.setFontSize(12);
            surnameLabelRun.setText(labels1.get(i));
            XWPFTableCell cell2 = clientTable.getRow(i).getCell(1);
            XWPFParagraph value = cell2.addParagraph();
            value.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun surnameActualRun = value.createRun();
            surnameActualRun.setFontFamily("Arial");
            surnameActualRun.setFontSize(12);
            surnameActualRun.setText(values1.get(i));
        }

        XWPFParagraph blank = document.createParagraph();
        XWPFRun blankRun = blank.createRun();
        blankRun.setText("");

        XWPFTable tableMain = document.createTable(4,3);
        tableMain.setWidthType(TableWidthType.PCT);
        tableMain.setWidth("100%");
        CTHMerge hMergeUpper = CTHMerge.Factory.newInstance();
        hMergeUpper.setVal(STMerge.RESTART);
        tableMain.getRow(0).getCell(0).getCTTc().addNewTcPr().setHMerge(hMergeUpper);
        CTHMerge hMergeUpper1 = CTHMerge.Factory.newInstance();
        hMergeUpper1.setVal(STMerge.CONTINUE);
        tableMain.getRow(0).getCell(1).getCTTc().addNewTcPr().setHMerge(hMergeUpper1);
        CTVMerge vmerge = CTVMerge.Factory.newInstance();
        vmerge.setVal(STMerge.RESTART);
        tableMain.getRow(0).getCell(2).getCTTc().addNewTcPr().setVMerge(vmerge);
        CTVMerge vmerge1 = CTVMerge.Factory.newInstance();
        vmerge1.setVal(STMerge.CONTINUE);
        tableMain.getRow(1).getCell(2).getCTTc().addNewTcPr().setVMerge(vmerge1);
        CTHMerge hMergeLower = CTHMerge.Factory.newInstance();
        hMergeLower.setVal(STMerge.RESTART);
        tableMain.getRow(3).getCell(0).getCTTc().addNewTcPr().setHMerge(hMergeLower);
        CTHMerge hMergeLower1 = CTHMerge.Factory.newInstance();
        hMergeLower1.setVal(STMerge.CONTINUE);
        tableMain.getRow(3).getCell(1).getCTTc().addNewTcPr().setHMerge(hMergeLower1);

        XWPFParagraph service = tableMain.getRow(0).getCell(0).addParagraph();
        service.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun serviceRun = service.createRun();
        serviceRun.setFontSize(12);
        serviceRun.setFontFamily("Arial");
        serviceRun.setText("За что получено");

        XWPFParagraph boxNumber = tableMain.getRow(1).getCell(0).addParagraph();
        boxNumber.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun boxNumberRun = boxNumber.createRun();
        boxNumberRun.setFontSize(12);
        boxNumberRun.setFontFamily("Arial");
        boxNumberRun.setText("Бокс номер");

        XWPFParagraph rentPeriod = tableMain.getRow(1).getCell(1).addParagraph();
        rentPeriod.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rentPeriodRun = rentPeriod.createRun();
        rentPeriodRun.setFontSize(12);
        rentPeriodRun.setFontFamily("Arial");
        rentPeriodRun.setText("Период аренды");

        XWPFParagraph sum = tableMain.getRow(0).getCell(2).addParagraph();
        sum.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun sumRun = sum.createRun();
        sumRun.setFontSize(12);
        sumRun.setFontFamily("Arial");
        sumRun.setText("Сумма, руб.");

        XWPFParagraph boxNumberValue = tableMain.getRow(2).getCell(0).addParagraph();
        boxNumberValue.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun boxNumberValueRun = boxNumberValue.createRun();
        boxNumberValueRun.setFontSize(12);
        boxNumberValueRun.setFontFamily("Arial");
        boxNumberValueRun.setText("" + boxId);

        XWPFParagraph rentPeriodValue = tableMain.getRow(2).getCell(1).addParagraph();
        rentPeriodValue.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rentPeriodValueRun = rentPeriodValue.createRun();
        rentPeriodValueRun.setFontSize(12);
        rentPeriodValueRun.setFontFamily("Arial");
        rentPeriodValueRun.setText("c " + RentController.getDateAsString(startDate) +
                "г. по " + RentController.getDateAsString(endDate) + "г.");

        XWPFParagraph sumValue = tableMain.getRow(2).getCell(2).addParagraph();
        sumValue.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun sumValueRun = sumValue.createRun();
        sumValueRun.setFontSize(12);
        sumValueRun.setFontFamily("Arial");
        sumValueRun.setText("" + sumPrice);

        XWPFParagraph finalSum = tableMain.getRow(3).getCell(0).addParagraph();
        finalSum.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun finalSumRun = finalSum.createRun();
        finalSumRun.setFontSize(12);
        finalSumRun.setFontFamily("Arial");
        finalSumRun.setText("Всего по квитанции");

        XWPFParagraph finalSumValue = tableMain.getRow(3).getCell(2).addParagraph();
        finalSumValue.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun finalSumValueRun = finalSumValue.createRun();
        finalSumValueRun.setFontSize(12);
        finalSumValueRun.setFontFamily("Arial");
        finalSumValueRun.setText("" + sumPrice);

        XWPFParagraph blank2 = document.createParagraph();
        XWPFRun blank2Run = blank2.createRun();
        blank2Run.setText("");

        List<String> signatureLabels = List.of("Сумма прописью", "В том числе наличными денежными средствами",
                "Оплатил арендатор", "Получил арендодатель");
        XWPFTable signaturesTable = document.createTable(signatureLabels.size(), 1);
        signaturesTable.setWidthType(TableWidthType.PCT);
        signaturesTable.setWidth("100%");
        signaturesTable.setTopBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        signaturesTable.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "000000");
        signaturesTable.setLeftBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        signaturesTable.setRightBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");
        signaturesTable.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "000000");
        signaturesTable.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, 1, 0, "000000");

        for (int i = 0; i < signatureLabels.size(); i++) {
            XWPFTableCell cell1 = signaturesTable.getRow(i).getCell(0);
            XWPFParagraph label = cell1.addParagraph();
            label.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun surnameLabelRun = label.createRun();
            surnameLabelRun.setFontFamily("Arial");
            surnameLabelRun.setFontSize(12);
            surnameLabelRun.setText(signatureLabels.get(i));
        }

        XWPFParagraph seal = document.createParagraph();
        seal.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun sealRun1 = seal.createRun();
        sealRun1.addBreak();
        sealRun1.setFontFamily("Arial");
        sealRun1.setFontSize(12);
        sealRun1.setText("М.П.\"");
        XWPFRun sealRun2 = seal.createRun();
        sealRun2.setUnderline(UnderlinePatterns.SINGLE);
        sealRun2.setText("   ");
        XWPFRun sealRun3 = seal.createRun();
        sealRun3.setFontSize(12);
        sealRun3.setFontFamily("Arial");
        sealRun3.setText("\"");
        XWPFRun sealRun4 = seal.createRun();
        sealRun4.setUnderline(UnderlinePatterns.SINGLE);
        sealRun4.setText("                        ");
        XWPFRun sealRun5 = seal.createRun();
        sealRun5.setFontSize(12);
        sealRun5.setFontFamily("Arial");
        sealRun5.setText("г.");

        String filename = "Квитанция_"+ receiptNumber +".docx";
        FileOutputStream out = new FileOutputStream(filename);
        document.write(out);
        out.close();
        document.close();
    }

    public static void generateNote(String title, List<List<String>> records, String filename) throws IOException {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph caption = document.createParagraph();
        caption.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun captionRun = caption.createRun();
        captionRun.setFontFamily("Arial");
        captionRun.setFontSize(16);
        captionRun.setBold(true);
        captionRun.setText(title);

        if (records == null || records.isEmpty()) {
            XWPFParagraph message = document.createParagraph();
            message.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun messageRun = message.createRun();
            messageRun.setFontFamily("Arial");
            messageRun.setFontSize(16);
            messageRun.setBold(true);
            messageRun.setText("Отсутствуют");
            FileOutputStream out = new FileOutputStream(filename);
            document.write(out);
            out.close();
            document.close();
            return;
        }
        XWPFTable table = document.createTable(records.size(), records.get(0).size() + 1);
        table.setWidthType(TableWidthType.PCT);
        table.setWidth("100%");
        for (int i = 0; i < records.size(); i++) {
            XWPFTableCell number = table.getRow(i).getCell(0);
            number.setWidthType(TableWidthType.AUTO);
            XWPFParagraph numberValue = number.addParagraph();
            numberValue.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun numberValueRun = numberValue.createRun();
            numberValueRun.setFontFamily("Arial");
            numberValueRun.setFontSize(12);
            numberValueRun.setText("" + (i + 1));
            for (int j = 0; j < records.get(0).size(); j++) {
                XWPFTableCell cell = table.getRow(i).getCell(j + 1);
                XWPFParagraph value = cell.addParagraph();
                value.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun valueRun = value.createRun();
                valueRun.setFontFamily("Arial");
                valueRun.setFontSize(12);
                valueRun.setText(records.get(i).get(j));
            }
        }

        FileOutputStream out = new FileOutputStream(filename);
        document.write(out);
        out.close();
        document.close();
    }



}
