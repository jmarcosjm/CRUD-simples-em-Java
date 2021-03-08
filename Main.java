import java.io.File;

public class Main {
    public static void main(String[] args)
    {
        byte ba[];
        Livro l1 = new Livro("Os batatinhas","Zezinho Quatrivint Costa",35.50F);
        Livro l2 = new Livro("Coraline", "Dona",28.90F);
        Livro l3 = new Livro("The Witcher Ultimate", "Andrzej Sapkowski", 99.50F);
        try{
            new File("livros.db").delete();
            CRUD<Livro> arq =  new CRUD<>(Livro.class.getConstructor(),"livros.db");

            // Insere os 3 livros
            System.out.println("Inserindo os 3 livros...\n");
            int id1 = arq.create(l1);
            int id2 = arq.create(l2);
            int id3 = arq.create(l3);

            // Busca 3 livros
            System.out.println("Buscando os 3 livros:\n");
            System.out.println(arq.read(id3));
            System.out.println(arq.read(id1));
            System.out.println(arq.read(id2));

            // Altera um livro para um tamanho maior e exibe o resultado
            System.out.println("Atualizando um livro para um tamanho maior e exibindo o resultado:\n");
            l2.setAutor("Neil Gaiman");
            l2.setTitulo("Coraline no mundo sombrio");
            l2.setPreco(3500.65F);
            arq.update(l2);
            System.out.println(arq.read(id2));

            // Altera um livro para um tamanho menor e exibe o resultado
            System.out.println("Atualizando um livro para um tamanho menor e exibindo o resultado:\n");
            l3.setAutor("Andrzej");
            l3.setTitulo("The Witcher");
            l3.setPreco(65.30F);
            arq.update(l3);
            System.out.println(arq.read(id3));

            // Exclui um livro e mostra que o mesmo não existe mais
            System.out.println("Excluindo um livro e mostrando que o mesmo não existe mais:\n");
            arq.delete(id1);
            Livro aux = arq.read(id1);
            if(aux == null) System.out.println("Livro excluído\n");
            else System.out.println(aux);

            // Tenta atualizar um registro excluído
            System.out.println("Tentando atualizar e mostrar um registro excluído:\n");
            l1.setTitulo("O fundo da rua");
            l1.setPreco(5.00F);
            arq.update(l1);
            System.out.println(arq.read(id1));

            // Encerrando o CRUD
            System.out.println("\nEncerrando o CRUD:\n");
            arq.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
