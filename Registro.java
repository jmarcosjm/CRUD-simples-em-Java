public interface Registro {
    public void setID(int ID);
    public int getID();
    public byte[] toByteArray() throws Exception;
    public void fromByteArray(byte[] ba) throws Exception;
}
