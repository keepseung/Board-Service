package hello.boardservice.service;

import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import hello.boardservice.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    private final UploadFileRepository uploadFileRepository;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Transactional
    public void saveFileAll(List<UploadFile> uploadFile){
        uploadFileRepository.saveAll(uploadFile);
    }

    @Transactional
    public void deleteBoardFile(Board board){
        uploadFileRepository.deleteUploadFileByBoard(board);
    }

    //업로드한 여러 파일을 순차적으로 서버에 저장한다.
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, Board board) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, board));

            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile, Board board) throws IOException{
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();

        // 파일 이름을 만들고 업로드할 경로에 저장한다.
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName, board);
    }

    // 파일 이름이 이미 업로드된 파일들과 겹치지 않게 UUID를 사용한다.
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 사용자가 업로드한 파일에서 확장자를 추출한다.
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
