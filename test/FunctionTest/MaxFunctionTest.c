int max(int a,int b){
    int t;
    if(a > b)
        t = a;
    else
        t = b;
    return t;
}

int min(int a,int b){
    int t;
    if(a < b)
        t = a;
    else
        t = b;
    return t;
}

int mmax(int a,int b,int c,int d){
    int t1,t2,t3;
    t1 = max(a,b);
    t2 = max(c,d);
    t3 = max(t1,t2);
    return t3;
}

void test(int a){
    int test = 1;
    int test2 = 2;
}

int main(){
    int v1 = 1,v2=2,v3=3,v4 =4;
    int t1 = 0,t2 = 0,t3 = 0;
    t1 = max(v1,v2);
    t2 = min(v3,v4);
    t3 = mmax(v1,v2,v3,v4);
    test(1);
    return 0;
}
