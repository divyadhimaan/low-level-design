//package spotify.strategy.recommend;
//
//import spotify.model.Song;
//import spotify.repository.SongRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class TrendingStrategy implements RecommendationStrategy {
//
//    private final SongRepository songRepository;
//
//    public TrendingStrategy(SongRepository songRepository) {
//        this.songRepository = songRepository;
//    }
//
//    @Override
//    public List<Song> recommend(Long userId) {
//        return songRepository.getAllSongs()
//                .stream()
//                .limit(5) // simulate trending
//                .collect(Collectors.toList());
//    }
//}
