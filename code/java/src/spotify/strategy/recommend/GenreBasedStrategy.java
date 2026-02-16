//package spotify.strategy.recommend;
//
//import spotify.model.Song;
//import spotify.repository.SongRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class GenreBasedStrategy implements RecommendationStrategy {
//
//    private final SongRepository songRepository;
//    private final String genre;
//
//    public GenreBasedStrategy(SongRepository songRepository, String genre) {
//        this.songRepository = songRepository;
//        this.genre = genre.toLowerCase();
//    }
//
//    @Override
//    public List<Song> recommend(Long userId) {
//        return songRepository.getAllSongs()
//                .stream()
//                .filter(song -> song.getGenre().toLowerCase().contains(genre))
//                .collect(Collectors.toList());
//    }
//}