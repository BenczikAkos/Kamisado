package init_generator;

public class Generator {
	
	public void frontNeighbourhoodGen() {
		for(int i = 1; i < 63; ++i) {
			if(i>=1 && i<=6) {
				System.out.printf(":::%d:%d:%d:%d:%d\n", i-1, i+1, i+7, i+8, i+9);
			}
			else if(i>=57 && i<=62) {
				System.out.printf("%d:%d:%d:%d:%d:::\n", i-9, i-8, i-7, i-1, i+1);
			}
			else if(i==0 || i==7 || i==56)
				continue;
			else if( (i%8) != 0 && (i%8) != 7){
				System.out.printf("%d:%d:%d:%d:%d:%d:%d:%d\n", i-9, i-8, i-7, i-1, i+1, i+7, i+8, i+9);
			}
			else if((i%8) == 7) {
				System.out.printf("%d:%d::%d::%d:%d:\n", i-9, i-8, i-1, i+7, i+8);
			}
		}
	}
	
	public void backNeighbourhoodGen() {
		for(int i = 0; i < 64; ++i) {
			if(i<=7)
				System.out.printf(":%d\n", i+8);
			else if(i>=56) {
				System.out.printf("%d:\n", i-8);
			}
			else {
				System.out.printf("%d:%d\n", i-8, i+8);
			}
		}
	}

	public static void main(String[] args) {
		Generator gen = new Generator();
		gen.backNeighbourhoodGen();
	}

}
