package io.expressive.pdftest

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.itextpdf.text.pdf.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var btn_gerar:Button
    lateinit var qt1:EditText
    lateinit var qt2:EditText
    lateinit var nome:EditText
    lateinit var telf:EditText
    lateinit var spinner1:Spinner
    lateinit var spinner2:Spinner
     var pagewidth:Int=1200
    lateinit  var date:Date
    lateinit var dateFormat: DateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_gerar = findViewById(R.id.btn_normal)
        qt1 = findViewById(R.id.txt_qtd1)
        qt2 = findViewById(R.id.txt_qtd2)
        nome = findViewById(R.id.txt_nome)
        telf = findViewById(R.id.txt_telefone)
        spinner1 = findViewById(R.id.sp1)
        spinner2 = findViewById(R.id.sp2)

    }

    fun onPdfGenerate(view: View){
        date = Date()

        gerarPdfNormal()
    }

    fun onPdfGenerateItext(view: View){
        gerarPdfItext()
    }

    fun gerarPdfNormal(){

        val doc:PdfDocument = PdfDocument();
        val paint:Paint = Paint();
        val titulo:Paint = Paint();

        val pageInfo:PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(pagewidth,2010,1).create()
        val page:PdfDocument.Page = doc.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        canvas.drawText("Olá mundo gerando pdf",40F,50F,paint)

        titulo.textAlign = Paint.Align.CENTER
        titulo.typeface = Typeface.DEFAULT_BOLD
        titulo.textSize = 70F

        canvas.drawText("Recibo",((pagewidth.div(2).toFloat())),270f,titulo)

        paint.color=Color.rgb(0,113,188)
        paint.textSize=30f
        paint.textAlign = Paint.Align.RIGHT

        canvas.drawText("Algum texto qualquer",1160f,40f,paint)
        canvas.drawText("Outro texto",1160f,80f,paint)

        titulo.textAlign = Paint.Align.CENTER
        titulo.typeface= Typeface.create(Typeface.DEFAULT,Typeface.ITALIC)
        titulo.textSize=70f
        canvas.drawText("Recibo",(pagewidth/2).toFloat(),500f,titulo)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 35f
        paint.color = Color.BLACK
        canvas.drawText("Cliente: ${nome.text}",20f,690f,paint)
        canvas.drawText("Telefone: ${telf.text}",20f,640f,paint)

        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("Recibo nº: #",(pagewidth-20).toFloat(),590f,paint)

        dateFormat=SimpleDateFormat("dd/mm/yy")
        canvas.drawText("Data: "+dateFormat.format(date), (pagewidth-20).toFloat(),640f,paint)

        dateFormat=SimpleDateFormat("HH:mm:ss")
        canvas.drawText("Hora: "+dateFormat.format(date),(pagewidth-20).toFloat(),690f,paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth=2f

        //Cabecalho
        canvas.drawRect(20f,780f,(pagewidth-20).toFloat(),860f,paint)
        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        canvas.drawText("Nª",40f,830f,paint)
        canvas.drawText("Descrição",200f,830f,paint)
        canvas.drawText("Preço",700f,830f,paint)
        canvas.drawText("Quantidade",900f,830f,paint)
        canvas.drawText("Total",1050f,830f,paint)

        canvas.drawLine(180f,790f,180f,840f,paint)
        canvas.drawLine(680f, 790f,680f,840f,paint)
        canvas.drawLine(880f, 790f, 880f,840f, paint)
        canvas.drawLine(1030f, 790f, 1030f,840f, paint)

        //Corpo OBJ 1
        canvas.drawText("1.",40f,950f,paint)
        canvas.drawText(spinner1.selectedItem.toString(),200f,950f,paint)
        canvas.drawText("200",700f,950f,paint)
        canvas.drawText(qt1.text.toString(),900f,950f,paint)

        paint.textAlign= Paint.Align.RIGHT
        canvas.drawText("200",(pagewidth-40).toFloat(),950f,paint)

        paint.textAlign= Paint.Align.LEFT
        //Corpo OBJ 2
        canvas.drawText("2.",40f,1050F,paint)
        canvas.drawText(spinner2.selectedItem.toString(),200f,1050F,paint)
        canvas.drawText("200",700f,1050F,paint)
        canvas.drawText(qt1.text.toString(),900f,950f,paint)

        paint.textAlign= Paint.Align.RIGHT
        canvas.drawText("200",(pagewidth-40).toFloat(),950f,paint)
        paint.textAlign= Paint.Align.LEFT

        //Resumo, total e subtotal
        canvas.drawLine(680f,1200f,(pagewidth-40).toFloat(),1200f,paint)
        canvas.drawText("Subtotal",700f,1250f,paint)
        canvas.drawText(":",900f, 1250f,paint)
        paint.textAlign= Paint.Align.RIGHT
        canvas.drawText("234",(pagewidth-40).toFloat(),1250f,paint)

        paint.textAlign= Paint.Align.LEFT
        canvas.drawText("Taxa (10%)",700f,1300f,paint)
        canvas.drawText(":",900f, 1300f,paint)
        paint.textAlign= Paint.Align.RIGHT
        canvas.drawText("400",(pagewidth-40).toFloat(),1300f,paint)

        paint.textAlign= Paint.Align.LEFT
        paint.color = Color.rgb(247,147,30)
        canvas.drawRect(680f,1350f,(pagewidth-20).toFloat(),1450f, paint)

        paint.color=Color.BLACK
        paint.textSize=50f
        paint.textAlign=Paint.Align.LEFT
        canvas.drawText("Total",700f,1415f,paint)
        paint.textAlign= Paint.Align.RIGHT
        canvas.drawText("400",(pagewidth-40).toFloat(),1415f,paint)

        doc.finishPage(page)

        val file:File = File(Environment.getExternalStorageDirectory(),"/normal.pdf")

        try {

            doc.writeTo(FileOutputStream(file))

            Log.d("PDF","O Arquivo foi gerado com sucesso")

        }catch (e: IOException){
            e.printStackTrace();
        }

        doc.close();

    }

    fun gerarPdfItext(){

        val file:File = File(Environment.getExternalStorageDirectory(),"/itext.pdf")
        val doc:com.itextpdf.text.pdf.PdfDocument = com.itextpdf.text.pdf.PdfDocument();
        val writer: PdfWriter=PdfWriter.getInstance(doc,FileOutputStream(file))

        writer.newPage()
        val _doc:Document=Document()
        _doc.close()

    }
}
