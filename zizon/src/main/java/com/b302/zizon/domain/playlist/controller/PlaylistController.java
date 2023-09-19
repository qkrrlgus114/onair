package com.b302.zizon.domain.playlist.controller;

import com.b302.zizon.domain.music.entity.Music;
import com.b302.zizon.domain.playlist.dto.AddPlaylistMusicDTO;
import com.b302.zizon.domain.playlist.dto.MakePlaylistRequestDTO;
import com.b302.zizon.domain.playlist.dto.PlaylistInfoResponseDTO;
import com.b302.zizon.domain.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaylistController {

    private final PlaylistService playlistService;

    // 플레이리스트에 음악 추가
    @PostMapping("playlist/music")
    public ResponseEntity<?> addPlaylistMusic(@RequestBody AddPlaylistMusicDTO addPlaylistMusicDTO){
        playlistService.addPlaylistMusic(addPlaylistMusicDTO);

        return ResponseEntity.status(HttpStatus.OK).body("플레이리스트 음악 추가 성공");
    }

    // 플레이리스트 생성하기
    @PostMapping("playlist")
    public ResponseEntity<?> makePlaylist(@RequestBody MakePlaylistRequestDTO makePlaylistRequestDTO){
        playlistService.MakePlaylist(makePlaylistRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body("플레이리스트 생성 성공");
    }

    // 플레이리스트 호출하기
    @GetMapping("playlist")
    public ResponseEntity<?> getPlaylist(){
        List<PlaylistInfoResponseDTO> playlist = playlistService.getPlaylist();

        return ResponseEntity.status(HttpStatus.OK).body(playlist);
    }

}