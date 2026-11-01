public class Card {
    private String rank;
    private String suit;
    public Card(String rank, String suit){
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString(){
        return (rank + " of " + suit);
    }

    @Override
    public boolean equals(Object o){
        Card sumn = (Card) o;
        return (this.rank == sumn.rank && this.suit == sumn.suit);
    }

    @Override
    public int hashCode(){
        return (rank+suit).hashCode();
    }

    public static void main(String[] args){

        System.out.println("\n Considering an array of card objects- 'Deck' ");
        Card Deck[] = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("10", "Clubs"),
            new Card("King", "Hearts"),
            new Card("7", "Diamonds")
        };

        System.out.println("\n -Printing the collection of card objects: ");
        for(int i=0;i<Deck.length;i++){
            System.out.println(" ->" + Deck[i].toString());
        }
        Card Final[] = new Card[5];
        int j=0;

        for(int i=0;i<Deck.length;i++){
            boolean isDuplicate = false;

            for(int k=0;k<i;k++){
                if(Deck[i].equals(Deck[k])){
                    System.out.println("\n ~Duplicate found: " + Deck[i]);
                    isDuplicate = true;
                    break;
                }
            }
            if(!isDuplicate){
                Final[j] = Deck[i];
                j++;
            }
        }
        System.out.println("\n");
    }
}
