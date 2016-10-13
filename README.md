# DistributedMutualExclusion

/**

Heitor Oliveira - 354065
Carolina Herbster - 354044

**/

Para executar o cliente: 

java client.Client <ip local> <ip do servidor> <porta>

Para executar o servidor:

java server.Server <ip local> <porta>

Todos os argumentos são opcionais. IPs padrão são localhost e porta padrão é a 3456.

O cliente faz requests automaticamente, com um intervalo de 10 segundos entre requests e mantendo o recurso por 0 a 8 segundos. Escreve no terminal quando pede, obtém e libera o recurso.

O servidor recebe requests e os coloca em uma fila. A cada request ele diz qual cliente fez o pedido e mostra o estado da fila. Ele também mostra no terminal quando o recurso é liberado.
