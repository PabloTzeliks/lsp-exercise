package service;

import model.IPedido;

public class ProcessadorPagamento {

    public void calcular(IPedido pedido) {

        double totalPagar = pedido.getValorFinal();

        System.out.println("Total Pagar: " + totalPagar);
        System.out.println();
        System.out.println("Processando: " + pedido.getNome());
    }
}
