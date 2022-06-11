package com.br.cesar.plantech;

import java.util.Random;

public class ResponseCalc {
	protected int questMock[] = {1, 2, 2, 0};
	
	public static int findResponse(int[] questions) {
		int[] returnData = new int[2];
		
		returnData = testMostOfAll(questions);
		
		if (returnData[0] == 0) {
			return randPairs(questions, returnData[1]);
		}else {
			//passou desde o primeiro teste (o maior de todos).
			return returnData[1];
		}
	}
	
	protected static int[] testMostOfAll(int[] questions) {
		int pos = 0;
		int max = questions[0];
		int countPairs = 0;
		int[] returnData = new int[2];
		/*
		 * Exemplo: 4 respostas em um único grupo de plantas e 1 para outro grupo, nesse caso o maior grupo será retornado.
		 *
		 * */
		
		for (int i = 1; i < 4; i++) {
			if (questions[i] > max) {
				pos = i;
				max = questions[i];
			}
		}
		
		for (int i = pos+1; i < 4; i++) {
			if (questions[i] == max) {
				countPairs++;
			}
		}
		
		System.out.println("O maior valor é: "+questions[pos]+ "\nMaior posição: "+pos);
		
		if (countPairs > 0) {
			returnData[0] = 0;
			returnData[1] = pos;
			return returnData;
		}else {
			returnData[0] = 1;
			returnData[1] = pos;
			return returnData;
		}
		
		//existe um maior que os outros? sim => pos, nao = -1

	}
	
	protected static int randPairs(int[] questions, int maxPos) {
		
		int[] arrayPair = new int[2];
		arrayPair[0] = maxPos;
		
		for (int i = maxPos+1; i < 4; i ++) {
			if (questions[maxPos] == questions[i]) {
				arrayPair[1]=i;
				break;
			}
		}
		
		Random rand = new Random();
		
		int posFinal = rand.nextInt(2);
		
		return arrayPair[posFinal];
		
	}
	
}
