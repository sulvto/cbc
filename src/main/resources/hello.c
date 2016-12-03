// import stdio;

struct point {
    int x;
    int y;
};

int add(int a,int b) {
    return a+b;
}

int main(int args) {
   // printf("Hello World!\n");
   int i=1;
   int j=2;
   int z = i+j;

   for(;i<z;i++) {
        j=z++;
        j=add(j,z);
   }
   return i+j+z;
}