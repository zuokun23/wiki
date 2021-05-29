export class Tool{

    /**
     * 空校验 null或者""都返回true
     * @param obj
     */
    public static isEmpty(obj : any){
        if((typeof obj === 'string')){
            //这个例子的意思就是将原字符串中的所有空白字符替换成”“，比如”abc d efg “字样的字符串使用这个函数后将变成”abcdefg”。
            return !obj || obj.replace(/\s/g, '') === '';
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }

    /**
     * 非空校验
     * @param obj
     */
    public static isNotEmpty(obj : any){
        return !this.isEmpty(obj);
    }

    /**
     * 对象复制
     * @param obj
     */
    public static copy(obj : object){
        if(Tool.isNotEmpty(obj)){
            return JSON.parse(JSON.stringify(obj));
        }
    }
}