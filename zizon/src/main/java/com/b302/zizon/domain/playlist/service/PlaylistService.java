package com.b302.zizon.domain.playlist.service;

import com.b302.zizon.domain.music.entity.Music;
import com.b302.zizon.domain.music.entity.MyMusicBox;
import com.b302.zizon.domain.music.repository.MusicRepository;
import com.b302.zizon.domain.music.repository.MyMusicBoxRepository;
import com.b302.zizon.domain.playlist.dto.AddPlaylistMusicDTO;
import com.b302.zizon.domain.playlist.dto.MakePlaylistRequestDTO;
import com.b302.zizon.domain.playlist.entity.Playlist;
import com.b302.zizon.domain.playlist.entity.PlaylistMeta;
import com.b302.zizon.domain.playlist.repository.PlaylistMetaRepository;
import com.b302.zizon.domain.playlist.repository.PlaylistRepository;
import com.b302.zizon.domain.user.entity.User;
import com.b302.zizon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    public Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Long userId = (Long) principal;

        return userId;
    }

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistMetaRepository playlistMetaRepository;
    private final MyMusicBoxRepository myMusicBoxRepository;
    private final MusicRepository musicRepository;

    // 플리에 음악 추가
    @Transactional
    public void addPlaylistMusic(AddPlaylistMusicDTO addPlaylistMusicDTO){
        Long userId = getUserId();

        Optional<User> byUserId = Optional.ofNullable(userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("pk에 해당하는 유저 존재하지 않음")));

        User user = byUserId.get();

        Long musicId = addPlaylistMusicDTO.getMusicId();
        Long playlistMetaId = addPlaylistMusicDTO.getPlaylistMetaId();

        Optional<Music> byMusic = musicRepository.findById(musicId);
        if(byMusic.isEmpty()){
            throw new IllegalArgumentException("노래 정보가 없습니다.");
        }
        Music music = byMusic.get();


        Optional<MyMusicBox> byMusicMusicIdAndUserUserId = myMusicBoxRepository.findByMusicMusicIdAndUserUserId(musicId, userId);
        if(byMusicMusicIdAndUserUserId.isEmpty()){
            throw new IllegalArgumentException("보관함에 없는 노래입니다.");
        }

        Optional<PlaylistMeta> byPlaylistMetaIdAndUserUserId = playlistMetaRepository.findByPlaylistMetaIdAndUserUserId(playlistMetaId, userId);
        if(byPlaylistMetaIdAndUserUserId.isEmpty()){
            throw new IllegalArgumentException("유저의 플레이리스트가 없습니다.");
        }

        PlaylistMeta playlistMeta = byPlaylistMetaIdAndUserUserId.get();

        Optional<Playlist> byPlaylistMetaPlaylistMetaIdAndMusicMusicId = playlistRepository.findByPlaylistMetaPlaylistMetaIdAndMusicMusicId(playlistMetaId, musicId);
        if(byPlaylistMetaPlaylistMetaIdAndMusicMusicId.isPresent()){
            throw new IllegalArgumentException("이미 플레이리스트에 추가된 음악입니다.");
        }

        // 플리 데이터 생성 후 저장
        Playlist playlist = Playlist.builder()
                .playlistMeta(playlistMeta)
                .music(music)
                .build();
        playlistRepository.save(playlist);

        if(playlistMeta.getPlaylistImage() == null){
            playlistMeta.registPlaylistImage(music.getAlbumCoverUrl());
        }
        playlistMeta.plusCountPlaylistCount();
    }
    
    // 플레이리스트 생성
    @Transactional
    public void MakePlaylist(MakePlaylistRequestDTO makePlaylistRequestDTO){
        Long userId = getUserId();

        Optional<User> byUserId = Optional.ofNullable(userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("pk에 해당하는 유저 존재하지 않음")));

        User user = byUserId.get();

        PlaylistMeta build = PlaylistMeta.builder()
                .playlistName(makePlaylistRequestDTO.getPlaylistName())
                .user(user)
                .playlistCount(0)
                .build();
        
        playlistMetaRepository.save(build);
    }
    
}
