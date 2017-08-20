package helper;

import org.springframework.data.repository.CrudRepository;

public interface MessageCodeRepository extends CrudRepository<MessageCode,Long> {
    MessageCode findMessageCodeByValue(int id);
}
