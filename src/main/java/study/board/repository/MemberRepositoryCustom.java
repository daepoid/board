package study.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.board.dto.MemberDTO;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberDTO> findMemberDTOList();

    Page<MemberDTO> findMemberDTOPage(Pageable pageable);
}
