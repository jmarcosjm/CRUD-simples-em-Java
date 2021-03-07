import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.DecimalFormat;

public class Livro implements Registro{
    protected int ID;
    protected String titulo;
    protected String autor;
    protected float preco;
    DecimalFormat df = new DecimalFormat("#,##0.00");

    public Livro(String titulo, String autor, float preco) {
        this.ID = -1;
        this.titulo = titulo;
        this.autor = autor;
        this.preco = preco;
    }

    public Livro() {
        this.ID = -1;
        this.titulo = null;
        this.autor = null;
        this.preco = 0.00F;
    }

    public void setID(int ID) {
        if(this.ID == -1) this.ID = ID;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    public int getID() {
        return ID;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public float getPreco() {
        return preco;
    }

    public DecimalFormat getDf() {
        return df;
    }

    @Override
    public String toString() {
        return "\nID: " + ID +
                "\nTítulo: " + titulo +
                "\nAutor: " + autor +
                "\nPreço: R$ " + df.format(preco) + "\n\n";
    }

    public byte[] toByteArray() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(ID);
        dos.writeUTF(titulo);
        dos.writeUTF(autor);
        dos.writeFloat(preco);
        dos.close();
        baos.close();
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        ID = dis.readInt();
        titulo = dis.readUTF();
        autor = dis.readUTF();
        preco = dis.readFloat();
    }


}
