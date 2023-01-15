package genres;

public class Genre {

    private String nameOfGenre;

    private int likes;

    public Genre(final String nameOfGenre, final int likes) {
        this.nameOfGenre = nameOfGenre;
        this.likes = likes;
    }

    public final String getNameOfGenre() {
        return nameOfGenre;
    }

    public final void setNameOfGenre(final String nameOfGenre) {
        this.nameOfGenre = nameOfGenre;
    }

    public final int getLikes() {
        return likes;
    }

    public final void setLikes(final int likes) {
        this.likes = likes;
    }
}
