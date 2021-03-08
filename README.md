# CRUD-simples-em-Java
**Um CRUD<generico> simples em Java que utiliza o Random Acess File como base. Para testar, foi utilzada a classe Livro.**

### Não implementado:

- Método `read` que recebe um critério específico, analogo a SELECT ID FROM Livro WHERE ID = 1

### O que foi feito:

- O método `setID()` só modifica o `ID` do objeto, caso o ID corrente seja **-1**, evitando alterações de IDs
- Os objetos sempre terão em seus construtores o valor de ID padrão **-1**
- Classe `CRUD` que gerencia os métodos de `create()`, `read()`, `update()` e `delete()`
- Classe `Livro` de modelo para o CRUD
- Interface Registro com os métodos `fromByteArray()`, `toByteArray()`, `getID()` e `setID()`
- Classe `Main` para testar as operações
- Create foi feito para aceitar somente objetos com id = -1, garantindo que nenhum objeto com id pré estabelecido seja inserido no arquivo de registros.
- Cada método printa na tela sempre que algo inesperado acontecer, como: Nenhum arquivo inserido no registro ou ID não encontrado.
- Método `close()` para fechar o `CRUD` / fechar o `Random Acess File`.

## Execução:

- Execute, na classe `Main.java`, o método:
```java
public static void main(String[] args)
```
