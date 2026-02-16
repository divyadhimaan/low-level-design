//package spotify.strategy.recommend;
//
//import spotify.model.Song;
//import spotify.repository.SongRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ArtistBasedStrategy implements RecommendationStrategy {
//
//    private final SongRepository songRepository;
//    private final Long artistId;
//
//    public ArtistBasedStrategy(SongRepository songRepository, Long artistId) {
//        this.songRepository = songRepository;
//        this.artistId = artistId;
//    }
//
//    @Override
//    public List<Song> recommend(Long userId) {
//        return songRepository.getAllSongs()
//                .stream()
//                .filter(song -> song.getArtists().contains(artistId))
//                .collect(Collectors.toList());
//    }
//}
