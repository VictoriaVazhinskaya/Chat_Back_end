package chat.model;

public class Message{
    private String author;
    private String text;
    private String date;
    private String id;

    public Message(String author, String text, String date, String id){
        this.author = author;
        this.text = text;
        this.date = date;
        this.id = id;
    }



    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String onConsole(){
        return this.date + " " + this.author + " : " + this.text;
    }

    public String toString() {
        return "{\"author\":\"" + this.author + "\" , \"text\":\"" + this.text + "\" , \"date\":\"" + this.date + "\" , \"id\":\"" + this.id +"\"}";
    }
}