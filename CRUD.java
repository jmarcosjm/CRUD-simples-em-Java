import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class CRUD<T extends Registro> {
    Constructor<T> constructor; // Contrutor para utilização nos objetos genericos
    protected RandomAccessFile arq; // Random Acess File que serivá como base do CRUD

    CRUD(Constructor<T> constructor, String fileName) throws Exception {
        this.constructor = constructor; // Atribui o contrutor para utilização nos objetos genericos
        arq = new RandomAccessFile(fileName,"rw"); // Gera o arquivo 
        arq.writeInt(0); // Escreve, no arquivo, o ultimo ID utilizado como 0 para inicialização
    }

    public void close() throws Exception { // Fecha o RandomAccessFile
        arq.close();
        System.out.println("O CRUD foi encerrado...");
    }

    public int create(T object) throws Exception {
        int newID = -1; // Declara variavel newID como valor inicial -1
        if(object.getID() == -1) {
            boolean keepReading = true; // Flag do do/while
            short regLength; // Tamanho do registro corrente
            int lastID; // Ultimo id lido
            long currentlyPos; // Posição do ultimo id lido
            byte[] reg; // Registro que receberá Array de bytes do objeto a ser gravado
            arq.seek(0); // Vai para a posição inicial
            lastID = arq.readInt();
            newID = lastID + 1;
            if(lastID > 0) {
                do {
                    arq.readByte(); //Pula a lapide já que não é necessessario saber se o objeto está ou não excluido
                    regLength = arq.readShort(); 
                    currentlyPos = arq.getFilePointer();
                    arq.seek(currentlyPos + regLength); // Vai até a lápide do proximo registro ou final do arquivo
                    if(arq.getFilePointer()>=arq.length()) keepReading = false; //Se chegar no final do arquivo, parar de ler
                }while(keepReading);
            }
            object.setID(newID);
            reg = object.toByteArray();
            arq.writeByte(0); // Cria a lápide do registro como não excluido
            arq.writeShort(reg.length); // Escreve o tamanho do registro
            arq.write(reg); // Escreve o registro
            arq.seek(0); // Vai para o inicio do arquivo
            arq.writeInt(newID); // Escreve o "novo ultimo" ID
        } else System.out.println("CREATE: ID do objeto invalido"); // Se o ID do objeto passado for diferente de -1
        return newID;
    }

    public T read(int ID) throws Exception
    {
        T object = null; // declara o objeto padrão como null
        if(ID > 0){ // Se o ID passado for maior que 0, ou seja, válido
            arq.seek(0); // Vai para a posição inicial
            int lastID = arq.readInt();
            if(lastID > 0)
            {
                long currentlyPos;
                boolean keepReading = true;
                boolean founded = false; // Variavel de controle para indicar se o registro foi ou não encontrado
                byte lapide; // Variavel que armazena o valor da lápide, permitindo controle acerca do objeto estar ou não excluido
                short regLength;
                int currentlyID;
                do{
                    lapide = arq.readByte();
                    regLength = arq.readShort();
                    currentlyPos = arq.getFilePointer();
                    currentlyID = arq.readInt();
                    arq.seek(currentlyPos + regLength);
                    if(arq.getFilePointer()>=arq.length()) keepReading = false;
                    if(lapide == 0 && currentlyID == ID){ //Se o objeto não estiver excluido e o id for o buscado
                        keepReading = false; // Para de ler
                        founded = true;
                        object = this.constructor.newInstance(); // Cria uma instancia de objeto utilizando o constructor
                        byte[] ba = new byte[regLength];
                        arq.seek(currentlyPos);
                        arq.read(ba); // Lê o registro
                        object.fromByteArray(ba); // Atribui os valores do registro ao objeto criado
                    }
                }while(keepReading);
                if(!founded) System.out.println("READ: O registro com o ID buscado não foi encontrado.");
            }else System.out.println("READ: Nenhum registro foi inserido no arquivo.");
        }else System.out.println("READ: ID fornecido invalido.");
        return object;
    }

    public boolean delete(int ID) throws Exception
    {
        boolean operationStatus = false;
        if(ID > 0){
            arq.seek(0);
            int lastID = arq.readInt();
            if(lastID > 0){
                long currentlyPos;
                long lapidePos;
                boolean keepReading = true;
                boolean founded = false;
                byte lapide;
                short regLength;
                int currentlyID;
                do{
                    lapidePos = arq.getFilePointer(); // Posição da lápide para futura alteração
                    lapide = arq.readByte();
                    regLength = arq.readShort();
                    currentlyPos = arq.getFilePointer();
                    currentlyID = arq.readInt();
                    arq.seek(currentlyPos + regLength);
                    if(arq.getFilePointer()>=arq.length()) keepReading = false;
                    if(lapide == 0 && ID == currentlyID) {
                        keepReading = false;
                        founded = true;
                        arq.seek(lapidePos); // Vai para a posição da lápide
                        arq.writeByte(1); // Marca o registro como excluido
                        operationStatus = true; // Sinaliza que a exclusão foi bem sucedida
                    }
                }while(keepReading);
                if(!founded) System.out.println("DELETE: O registro com o ID buscado não foi encontrado.");
            }else System.out.println("DELETE: A exclusão do registro não foi concluida. Nenhum registro foi inserido no arquivo.");
        }else System.out.println("DELETE: A exclusão do registro não foi concluida. O ID fornecido é invalido.");
        return operationStatus;
    }

    public boolean update(T object) throws Exception
    {
        boolean operationStatus = false;
        int ID = object.getID(); // Pega o ID do objeto passado
        if(ID > 0){
            arq.seek(0);
            int lastID = arq.readInt();
            if(lastID > 0){
                long lapidePos;
                long currentlyPos;
                boolean bigger = false; // Variavel de controle que indica se o novo registro é maior que o atual
                boolean keepReading = true;
                boolean founded = false;
                byte lapide;
                short regLength;
                int currentlyID;
                byte[] newBa = object.toByteArray();
                do {
                    lapidePos = arq.getFilePointer();
                    lapide = arq.readByte();
                    regLength = arq.readShort();
                    currentlyPos = arq.getFilePointer();
                    currentlyID = arq.readInt();
                    arq.seek(currentlyPos + regLength);
                    if(arq.getFilePointer() >= arq.length()) keepReading = false;
                    if(lapide == 0 && ID == currentlyID)
                    {
                        operationStatus = true;
                        founded = true;
                        byte[] oldBa = new byte[regLength];
                        arq.seek(currentlyPos);
                        arq.read(oldBa); // Lê o registro antigo
                        if(newBa.length <= oldBa.length) // Verifica se o novo registro é menor ou igual ao atual 
                        {
                            keepReading = false;
                            arq.seek(currentlyPos);
                            arq.write(newBa); // Escreve o novo registro por cima do atual
                        }else{
                            bigger = true; // Sinaliza que o novo registro é maior que o atual
                            arq.seek(lapidePos); // Vai até a posição da lápide
                            arq.writeByte(1); // Marca o registro atual como excluido para escrever o novo no final do arquivo;
                            arq.seek(currentlyPos + regLength); // Vai até a lápide do proximo registro ou final do arquivo
                        }
                    }
                }while(keepReading);
                if(bigger){
                  // Agora que o ponteiro está na ultima posição do arquivo
                    arq.writeByte(0); // Cria a lapide
                    arq.writeShort(newBa.length); // Escreve o tamanho do registro
                    arq.write(newBa); // Escreve o novo registro
                }
                if(!founded) System.out.println("UPDATE: O registro com o ID buscado não foi encontrado.");
            }else System.out.println("UPDATE: Nenhum registro foi inserido no arquivo.");
        } else System.out.println("UPDATE: A atualizaçãp do registro não foi concluida. O ID fornecido é invalido.");
        return operationStatus;
    }
}