package com.example.crud.controller;

import com.example.crud.domain.Member;
import com.example.crud.repository.MemberRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // REST API용 컨트롤러임을 선언
@RequestMapping("/api/members") // 이 주소로 들어오는 요청을 담당하도록 함
public class MemberController {

    private final MemberRepository repository;

    // 생성자 주입: 스프링이 Repository를 자동으로 연결
    public MemberController(MemberRepository repository) {
        this.repository = repository;
    }

    // 회원 등록 (POST 요청 처리)
    @PostMapping
    public Member save(@RequestBody Member member) {
        // @RequestBody: 요청으로 보낸 JSON 데이터를 Member 객체로 변환
        return repository.save(member);
    }

    // 전체 회원 조회 (GET 요청 처리)
    @GetMapping
    public List<Member> findAll() {
        return repository.findAll();
    }

    // 회원 정보 수정 (PUT 요청 처리)
    @PutMapping("/{id}")
    public Member update(@PathVariable Long id, @RequestBody Member memberDetails) {
        // 수정할 회원을 ID로 탐색 (없으면 에러 발생)
        Member member = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다. id=" + id));

        // 찾은 회원의 정보를 새로운 정보로 교체
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());

        // 변경된 정보를 저장
        return repository.save(member);
    }

    // 회원 삭제 (DELETE 요청 처리)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        // URL에 담긴 {id} 값을 읽어와서 해당 데이터를 삭제
        repository.deleteById(id);
    }
}
