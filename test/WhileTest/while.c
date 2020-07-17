int main(){
    int i,size=10,t = 0;
    int num[size];
    for(i = 0 ; i < size ; ++i){
        num[i] = 0;
    }
    while(t < size){
        num[t] = size-t;
        t++;
    }
    return 0;
}