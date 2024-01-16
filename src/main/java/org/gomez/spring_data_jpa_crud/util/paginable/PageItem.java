package org.gomez.spring_data_jpa_crud.util.paginable;

// Clase que representa un elemento de la paginación
public class PageItem {
    private int numero;      // Número de la página
    private boolean actual;  // Indica si es la página actual

    // Constructor que recibe el número de la página y si es la página actual
    public PageItem(int numero, boolean actual) {
        this.numero = numero;
        this.actual = actual;
    }

    // Método para obtener el número de la página
    public int getNumero() {
        return numero;
    }

    // Método para verificar si es la página actual
    public boolean isActual() {
        return actual;
    }
}
