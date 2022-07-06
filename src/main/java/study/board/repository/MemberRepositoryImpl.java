package study.board.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.board.dto.MemberDTO;
import study.board.dto.QMemberDTO;

import java.util.List;

import static study.board.domain.QMember.*;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberDTO> findMemberDTOList() {
        return queryFactory
                .select(new QMemberDTO(
                        member.id,
                        member.username,
                        member.loginId,
                        member.memberAuth))
                .from(member)
                .fetch();
    }

    @Override
    public Page<MemberDTO> findMemberDTOPage(Pageable pageable) {
        List<MemberDTO> content = queryFactory
                .select(new QMemberDTO(
                        member.id,
                        member.username,
                        member.loginId,
                        member.memberAuth))
                .from(member)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
