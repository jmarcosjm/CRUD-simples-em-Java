import java.io.File;

public class Main {
    public static void main(String[] args)
    {
        byte ba[];
        Livro l1 = new Livro("Os batatinhas","Zezinho Quatrivint Costa",35.50F);
        Livro l2 = new Livro("Coraline", "Dona",28.90F);
        Livro l3 = new Livro("The Witcher", "Andrzej Sapkowski", 67.50F);
        try{
            new File("livros.db").delete();
            CRUD<Livro> arq =  new CRUD<>(Livro.class.getConstructor(),"livros.db");

            // Insere os 3 livros
            int id1 = arq.create(l1);
            int id2 = arq.create(l2);
            int id3 = arq.create(l3);

            // Busca 2 livros
            System.out.println(arq.read(id3));
            System.out.println(arq.read(id1));

            // Altera um livro para um tamanho maior e exibe o resultado
            l2.setAutor("Neil Gaiman");
            arq.update(l2);
            System.out.println(arq.read(id2));

            // Altera um livro para um tamanho menor e exibe o resultado
            l3.setAutor("Andrzej");
            arq.update(l3);
            System.out.println(arq.read(id3));

            // Exclui um livro e mostra que o mesmo não existe mais
            arq.delete(id3);
            Livro aux = arq.read(id3);
            if(aux == null) System.out.println("Livro excluído");
            else System.out.println(aux);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
