package test;

import java.math.BigDecimal;

public class SqrtTest {
	public static void main(String[] args) {
		Double n = 2.0;
		BigDecimal num = new BigDecimal(n);

		BigDecimal bef;
		BigDecimal aft;

		for (int i = 0;; i++) {
			if (i * i > n) {
				bef = new BigDecimal(i - 1);
				aft = new BigDecimal(i);
				break;
			}
		}

		BigDecimal res = null;
		BigDecimal resup = new BigDecimal(0);

		BigDecimal temp = new BigDecimal(2);
		while (true) {
			res = bef.add(aft).divide(temp);

			String r1 = resup.toString();

			if (res.multiply(res).compareTo(num) > 0) {
				aft = res;
			} else {
				bef = res;
			}

			String r2 = res.toString();

			int count = 0;

			int start = r1.indexOf('.') + 1;

			resup = res.multiply(new BigDecimal(1));

			while (count < r1.length() && r1.charAt(count) == r2.charAt(count))
				count++;

			if (count == 1000 + start)// 1000Ϊ����λ��
			{
				System.out.println("   ---"
						+ res.toString().substring(0, count));
				break;
			}

			System.out.println(res.toString());

		}

	}
}