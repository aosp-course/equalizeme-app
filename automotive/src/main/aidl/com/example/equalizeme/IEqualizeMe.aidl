// IEqualizeMe.aidl
package com.example.equalizeme;

interface IEqualizeMe {
    /**
     * @brief Define o nível de graves.
     *
     * Esta função define o nível de graves para o valor especificado, se ele estiver dentro do intervalo válido de 0 a 10.
     *
     * @param value O nível de graves desejado.
     * @return true se o valor foi definido com sucesso, false caso contrário (por exemplo, se o valor estiver fora do intervalo).
     */
    boolean setBass(int value);

    /**
     * @brief Define o nível de médio.
     *
     * Esta função define o nível de médio para o valor especificado, se ele estiver dentro do intervalo válido de 0 a 10.
     *
     * @param value O nível de médio desejado.
     * @return true se o valor foi definido com sucesso, false caso contrário (por exemplo, se o valor estiver fora do intervalo).
     */
    boolean setMid(int value);

    /**
     * @brief Define o nível de agudos.
     *
     * Esta função define o nível de agudos para o valor especificado, se ele estiver dentro do intervalo válido de 0 a 10.
     *
     * @param value O nível de agudos desejado.
     * @return true se o valor foi definido com sucesso, false caso contrário (por exemplo, se o valor estiver fora do intervalo).
     */
    boolean setTreble(int value);
}