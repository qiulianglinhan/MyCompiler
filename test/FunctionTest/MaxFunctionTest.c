int max(int a,int b){
    int t;
    if(a > b)
        t = a;
    else
        t = b;
    return t;
}

int main(){
    int c = 1,d=2;
    int t = 0;
    t = max(c,d);
    return 0;
}
