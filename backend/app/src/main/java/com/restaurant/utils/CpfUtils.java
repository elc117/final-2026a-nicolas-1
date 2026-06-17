package com.restaurant.utils;

import java.util.regex.Pattern;

public class CpfUtils {
    
    private static final Pattern CPF_PATTERN = Pattern.compile("^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$");

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        if(CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        // Limpa o CPF, deixa somente os numeros
        String onlyNumbers = cpf.replaceAll("\\D", "");

        // CPF nao pode ter todos os numeros iguais
        if(onlyNumbers.matches("(\\d)\\1{10}")) {
            return false;
        }

        // validacao matematica
        try {
            return verifyCpfMath(onlyNumbers);
        } catch (Exception e) {
            return false;
        }
    }


    private static boolean verifyCpfMath(String onlyNumbers) {
        return (calculateVeryfingDigit(onlyNumbers, 9) == Character.getNumericValue(onlyNumbers.charAt(9)))
            && (calculateVeryfingDigit(onlyNumbers, 10) == Character.getNumericValue(onlyNumbers.charAt(10))); 
    }

    private static int calculateVeryfingDigit(String str, int maxWeight) {
        int sum = 0;
        int weight = maxWeight + 1;
        for(int i = 0; i < maxWeight; i++) {
            sum += Character.getNumericValue(str.charAt(i)) * weight--;
        }
        int rest = sum % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }


}
