int main(){
    int num[10];
    int t,i = 0,j;
    for(i = 0 ; i < 10 ; i = i+1){
        num[i] = 10-i;
    }
    i = 0;
    for(; i < 10 ; i++){
        for(j = i+1 ; j < 10 ; ++j ){
            if(num[i] > num[j]){
                t = num[i];
                num[i] = num[j];
                num[j] = t;
            }
        }
    }
    return 0;
}
